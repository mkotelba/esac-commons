package com.esacinc.commons.test.crypto.ssl.revocation.impl;

import com.esacinc.commons.crypto.net.mime.EsacCryptoMediaTypes;
import com.esacinc.commons.utils.EsacEnumUtils;
import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.credential.Credential;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils;
import com.esacinc.commons.crypto.digest.utils.EsacDigestUtils.EsacDigestAlgorithmIdentifierFinder;
import com.esacinc.commons.crypto.ssl.revocation.OcspCertificateStatusType;
import com.esacinc.commons.crypto.ssl.revocation.OcspResponseStatusType;
import com.esacinc.commons.crypto.ssl.revocation.impl.EsacCertificateId;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.test.beans.impl.AbstractEsacHttpServer;
import com.esacinc.commons.test.crypto.ssl.revocation.OcspServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPRespBuilder;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPRespBuilder;
import org.bouncycastle.cert.ocsp.RespID;
import org.bouncycastle.cert.ocsp.jcajce.JcaRespID;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcContentSignerBuilder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.OrderComparator;
import org.springframework.http.MediaType;

public class OcspServerImpl extends AbstractEsacHttpServer implements OcspServer {
    private class OcspCredentialWrapper {
        private OcspCredentialWrapper issuerCredWrapper;
        private Credential cred;
        private X509Certificate cert;
        private X509CertificateHolder certHolder;
        private EsacCertificateId certId;
        private CertificateStatus certStatus;
        private RespID responderId;
        private ContentSigner contentSigner;

        public OcspCredentialWrapper(Credential cred) throws CertificateEncodingException, IOException, OperatorCreationException, OCSPException {
            this(null, cred);
        }

        public OcspCredentialWrapper(@Nullable OcspCredentialWrapper issuerCredWrapper, Credential cred)
            throws CertificateEncodingException, IOException, OperatorCreationException, OCSPException {
            CredentialInfo credInfo = (this.cred = cred).getInfo();
            // noinspection ConstantConditions
            CertificateInfo certInfo = credInfo.getCertificateDescriptor();

            this.cert = certInfo.getService();
            this.certHolder = certInfo.getHolder();

            boolean credIssuer = !this.cred.hasIssuerCredential();

            // noinspection ConstantConditions
            this.certId = new EsacCertificateId(EsacDigestUtils.CALC_PROV.get(OcspServerImpl.this.digestAlgId),
                (credIssuer ? this.certHolder : (this.issuerCredWrapper = issuerCredWrapper).getCertificateHolder()), this.cert.getSerialNumber());

            this.certStatus = this.cred.getConfig().getCertificateStatus();

            if (credIssuer) {
                // noinspection ConstantConditions
                this.responderId = new JcaRespID(this.cert.getSubjectX500Principal());
                // noinspection ConstantConditions
                this.contentSigner =
                    OcspServerImpl.this.contentSignerBuilder.build(PrivateKeyFactory.createKey(credInfo.getKeyPairDescriptor().getPrivateKey().getEncoded()));
            }
        }

        public X509Certificate getCertificate() {
            return this.cert;
        }

        public X509CertificateHolder getCertificateHolder() {
            return this.certHolder;
        }

        public EsacCertificateId getCertificateId() {
            return this.certId;
        }

        @Nullable
        public CertificateStatus getCertificateStatus() {
            return this.certStatus;
        }

        @Nullable
        public ContentSigner getContentSigner() {
            return this.contentSigner;
        }

        public Credential getCredential() {
            return this.cred;
        }

        @Nullable
        public OcspCredentialWrapper getIssuerCredentialWrapper() {
            return this.issuerCredWrapper;
        }

        @Nullable
        public RespID getResponderId() {
            return this.responderId;
        }
    }

    private class OcspServerRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private Extensions exts;
        private OcspCredentialWrapper credWrapper;
        private OcspCredentialWrapper issuerCredWrapper;

        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            LOGGER.error("Unable to process OCSP request.", cause);

            if ((this.exts == null) || (this.credWrapper == null) || (this.issuerCredWrapper == null)) {
                this.writeResponse(context, HttpResponseStatus.INTERNAL_SERVER_ERROR);

                return;
            }

            // noinspection ConstantConditions
            BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(this.credWrapper.getIssuerCredentialWrapper().getResponderId());
            respBuilder.setResponseExtensions(this.exts);

            BcContentSignerBuilder contentSignerBuilder = new BcRSAContentSignerBuilder(OcspServerImpl.this.sigAlgId, OcspServerImpl.this.digestAlgId);
            contentSignerBuilder.setSecureRandom(OcspServerImpl.this.secureRandom);

            // noinspection ConstantConditions
            this.writeResponse(context,
                RESP_WRAPPER_BUILDER.build(OcspResponseStatusType.INTERNAL_ERROR.getTag(),
                    respBuilder.build(
                        contentSignerBuilder.build(
                            PrivateKeyFactory.createKey(this.issuerCredWrapper.getCredential().getInfo().getKeyPairDescriptor().getPrivateKey().getEncoded())),
                        ArrayUtils.toArray(this.credWrapper.getCertificateHolder()), new Date()))
                    .getEncoded());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpRequest reqMsg) throws Exception {
            synchronized (OcspServerImpl.this) {
                if (!OcspServerImpl.this.credsInitialized) {
                    OcspServerImpl.this.issuerCredWrappers = new HashMap<>();
                    OcspServerImpl.this.credWrappers = new HashMap<>();

                    OcspCredentialWrapper credWrapper;

                    for (Credential issuerCred : OcspServerImpl.this.creds.stream().filter(cred -> (!cred.hasIssuerCredential() && cred.hasInfo()))
                        .toArray(Credential[]::new)) {
                        OcspServerImpl.this.issuerCredWrappers.put((credWrapper = new OcspCredentialWrapper(issuerCred)).getCertificate(), credWrapper);
                    }

                    for (Credential cred : OcspServerImpl.this.creds.stream().filter(cred -> (cred.hasIssuerCredential() && cred.hasInfo()))
                        .toArray(Credential[]::new)) {
                        // noinspection ConstantConditions
                        OcspServerImpl.this.credWrappers.put((credWrapper = new OcspCredentialWrapper(
                            OcspServerImpl.this.issuerCredWrappers.get(cred.getIssuerCredential().getInfo().getCertificateDescriptor().getService()), cred))
                                .getCertificateId(),
                            credWrapper);
                    }

                    OcspServerImpl.this.credsInitialized = true;
                }
            }

            if (!reqMsg.method().equals(HttpMethod.POST)) {
                this.writeResponse(context, HttpResponseStatus.METHOD_NOT_ALLOWED);

                return;
            }

            HttpHeaders reqMsgHeaders = reqMsg.headers();

            if (!reqMsgHeaders.contains(HttpHeaderNames.CONTENT_TYPE) ||
                !MediaType.valueOf(reqMsgHeaders.get(HttpHeaderNames.CONTENT_TYPE)).equals(EsacCryptoMediaTypes.OCSP_REQ)) {
                this.writeResponse(context, HttpResponseStatus.BAD_REQUEST);

                return;
            }

            OCSPReq req = new OCSPReq(reqMsg.content().copy().array());
            this.exts = new Extensions(req.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce));

            EsacCertificateId reqCertId = new EsacCertificateId(req.getRequestList()[0].getCertID()), certId = null;
            DigestCalculator reqDigestCalc = EsacDigestUtils.CALC_PROV.get(new AlgorithmIdentifier(reqCertId.getHashAlgOID()));
            BigInteger certSerialNum = reqCertId.getSerialNumber();

            for (EsacCertificateId availableCertId : OcspServerImpl.this.credWrappers.keySet()) {
                if (reqCertId.matches(availableCertId)) {
                    certId = availableCertId;

                    break;
                }
            }

            if (certId == null) {
                throw new OCSPException(String.format("Unable to match OCSP request certificate (serialNum=%d).", certSerialNum));
            }

            this.issuerCredWrapper = (this.credWrapper = OcspServerImpl.this.credWrappers.get(certId)).getIssuerCredentialWrapper();

            X509Certificate cert = this.credWrapper.getCertificate();
            String certSubjectDnName = cert.getSubjectX500Principal().getName(), certIssuerDnName = cert.getIssuerX500Principal().getName();
            CertificateStatus certStatus = this.credWrapper.getCertificateStatus();

            // noinspection ConstantConditions
            BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(this.issuerCredWrapper.getResponderId());
            respBuilder.setResponseExtensions(this.exts);
            respBuilder.addResponse(certId, certStatus);

            BcContentSignerBuilder contentSignerBuilder = new BcRSAContentSignerBuilder(OcspServerImpl.this.sigAlgId, OcspServerImpl.this.digestAlgId);
            contentSignerBuilder.setSecureRandom(OcspServerImpl.this.secureRandom);

            // noinspection ConstantConditions
            this.writeResponse(context,
                RESP_WRAPPER_BUILDER.build(OcspResponseStatusType.SUCCESSFUL.getTag(),
                    respBuilder.build(
                        contentSignerBuilder.build(
                            PrivateKeyFactory.createKey(this.issuerCredWrapper.getCredential().getInfo().getKeyPairDescriptor().getPrivateKey().getEncoded())),
                        ArrayUtils.toArray(this.credWrapper.getCertificateHolder()), new Date()))
                    .getEncoded());

            LOGGER.debug(new MarkerBuilder(EsacLogstashTags.SSL).build(),
                String.format("Wrote OCSP response (status=%s) certificate (subjectDnName=%s, issuerDnName=%s, serialNum=%d) response (status=%s).",
                    OcspResponseStatusType.SUCCESSFUL.name(), certSubjectDnName, certIssuerDnName, certSerialNum,
                    EsacEnumUtils.findByType(Stream.of(OcspCertificateStatusType.class.getEnumConstants()).sorted(OrderComparator.INSTANCE),
                        ((certStatus != null) ? certStatus.getClass() : CertificateStatus.class))));
        }

        private void writeResponse(ChannelHandlerContext context, HttpResponseStatus respMsgStatus) {
            this.writeResponse(context, respMsgStatus, Unpooled.EMPTY_BUFFER);
        }

        private void writeResponse(ChannelHandlerContext context, byte[] respContent) {
            this.writeResponse(context, HttpResponseStatus.OK, Unpooled.wrappedBuffer(respContent));
        }

        private void writeResponse(ChannelHandlerContext context, HttpResponseStatus respMsgStatus, ByteBuf respContentBuffer) {
            FullHttpResponse respMsg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, respMsgStatus, respContentBuffer);

            HttpUtil.getContentLength(respMsg, respContentBuffer.array().length);
            respMsg.headers().set(HttpHeaderNames.CONTENT_TYPE, EsacCryptoMediaTypes.OCSP_RESP_VALUE);

            context.writeAndFlush(respMsg).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private final static OCSPRespBuilder RESP_WRAPPER_BUILDER = new OCSPRespBuilder();

    private final static Logger LOGGER = LoggerFactory.getLogger(OcspServerImpl.class);

    @Autowired
    @Lazy
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<Credential> creds;

    private SecureRandom secureRandom;
    private AlgorithmIdentifier sigAlgId;
    private AlgorithmIdentifier digestAlgId;
    private BcContentSignerBuilder contentSignerBuilder;
    private boolean credsInitialized;
    private Map<X509Certificate, OcspCredentialWrapper> issuerCredWrappers;
    private Map<EsacCertificateId, OcspCredentialWrapper> credWrappers;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.digestAlgId = EsacDigestAlgorithmIdentifierFinder.INSTANCE.find(this.sigAlgId);

        this.contentSignerBuilder = new BcRSAContentSignerBuilder(this.sigAlgId, this.digestAlgId);
        this.contentSignerBuilder.setSecureRandom(this.secureRandom);

        super.afterPropertiesSet();
    }

    @Override
    protected void stopInternal() {
        super.stopInternal();

        LOGGER.info(String.format("Stopped OCSP server (hostAddr={%s}, port=%d).", this.hostAddr, this.port));
    }

    @Override
    protected void startInternal() {
        super.startInternal();

        LOGGER.info(String.format("Started OCSP server (hostAddr={%s}, port=%d).", this.hostAddr, this.port));
    }

    @Override
    protected void initializeRequestChannel(NioSocketChannel reqChannel, ChannelPipeline reqChannelPipeline) {
        super.initializeRequestChannel(reqChannel, reqChannelPipeline);

        reqChannelPipeline.addLast(new OcspServerRequestHandler());
    }

    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    @Override
    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public AlgorithmIdentifier getSignatureAlgorithmId() {
        return this.sigAlgId;
    }

    @Override
    public void setSignatureAlgorithmId(AlgorithmIdentifier sigAlgId) {
        this.sigAlgId = sigAlgId;
    }
}
