package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.crypto.beans.ObjectIdentifiedBean;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.KeyPurposeId;

public enum ExtendedKeyUsageType implements ObjectIdentifiedBean {
    ANY(KeyPurposeId.anyExtendedKeyUsage), SERVER_AUTH(KeyPurposeId.id_kp_serverAuth), CLIENT_AUTH(KeyPurposeId.id_kp_clientAuth),
    CODE_SIGNING(KeyPurposeId.id_kp_codeSigning), EMAIL_PROTECTION(KeyPurposeId.id_kp_emailProtection), IPSEC_END_SYSTEM(KeyPurposeId.id_kp_ipsecEndSystem),
    IPSEC_TUNNEL(KeyPurposeId.id_kp_ipsecTunnel), IPSEC_USER(KeyPurposeId.id_kp_ipsecUser), TIME_STAMPING(KeyPurposeId.id_kp_timeStamping),
    OCSP_SIGNING(KeyPurposeId.id_kp_OCSPSigning), DVCS(KeyPurposeId.id_kp_dvcs), SBGP_CERT_AA_SERVER_AUTH(KeyPurposeId.id_kp_sbgpCertAAServerAuth),
    SCVP_RESPONDER(KeyPurposeId.id_kp_scvp_responder), EAP_OVER_PPP(KeyPurposeId.id_kp_eapOverPPP), EAP_OVER_LAN(KeyPurposeId.id_kp_eapOverLAN),
    SCVP_SERVER(KeyPurposeId.id_kp_scvpServer), SCVP_CLIENT(KeyPurposeId.id_kp_scvpClient), IPSEC_IKE(KeyPurposeId.id_kp_ipsecIKE),
    CAPWAP_AC(KeyPurposeId.id_kp_capwapAC), CAPWAP_WTP(KeyPurposeId.id_kp_capwapWTP), SMARTCARD_LOGON(KeyPurposeId.id_kp_smartcardlogon);

    private final ASN1ObjectIdentifier oid;

    private ExtendedKeyUsageType(KeyPurposeId keyPurposeId) {
        this.oid = keyPurposeId.toOID();
    }

    @Override
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
}
