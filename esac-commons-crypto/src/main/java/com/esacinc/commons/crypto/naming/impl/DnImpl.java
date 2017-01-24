package com.esacinc.commons.crypto.naming.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.esacinc.commons.crypto.naming.Dn;
import com.esacinc.commons.crypto.naming.utils.EsacX500Utils;
import java.util.Comparator;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;

public class DnImpl implements Dn {
    private ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> attrs;

    public DnImpl() {
        this(ArrayListMultimap.create());
    }

    public DnImpl(@Nullable X500Name x500Name) {
        this(EsacX500Utils.mapAttributes(x500Name));
    }

    private DnImpl(ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String toString() {
        return this.toX500Name().toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((obj != null) && (obj instanceof Dn) && this.toX500Name().equals(((Dn) obj).toX500Name()));
    }

    @Override
    public int hashCode() {
        return this.toX500Name().hashCode();
    }

    @Override
    public X500Name toX500Name() {
        return this.toX500Name(null);
    }

    @Override
    public X500Name toX500Name(@Nullable Comparator<ASN1ObjectIdentifier> attrOidComparator) {
        return EsacX500Utils.buildName(attrOidComparator, this.attrs);
    }

    private void setAttributeStringValue(ASN1ObjectIdentifier attrOid, @Nullable String attrStrValue) {
        if (attrStrValue != null) {
            this.attrs.put(attrOid, EsacX500Utils.toEncodableAttributeValue(attrOid, attrStrValue));
        } else {
            this.attrs.removeAll(attrOid);
        }
    }

    @Nullable
    private String getAttributeStringValue(ASN1ObjectIdentifier attrOid) {
        return (this.attrs.containsKey(attrOid) ? EsacX500Utils.toStringAttributeValue(this.attrs.get(attrOid).get(0)) : null);
    }

    @Override
    public ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> getAttributes() {
        return this.attrs;
    }

    @Override
    public boolean hasCommonName() {
        return this.attrs.containsKey(BCStrictStyle.CN);
    }

    @Nullable
    @Override
    public String getCommonName() {
        return this.getAttributeStringValue(BCStrictStyle.CN);
    }

    @Override
    public void setCommonName(@Nullable String commonName) {
        this.setAttributeStringValue(BCStrictStyle.CN, commonName);
    }

    @Override
    public boolean hasCountry() {
        return this.attrs.containsKey(BCStrictStyle.C);
    }

    @Nullable
    @Override
    public String getCountry() {
        return this.getAttributeStringValue(BCStrictStyle.C);
    }

    @Override
    public void setCountry(@Nullable String country) {
        this.setAttributeStringValue(BCStrictStyle.C, country);
    }

    @Override
    public boolean hasLocality() {
        return this.attrs.containsKey(BCStrictStyle.L);
    }

    @Nullable
    @Override
    public String getLocality() {
        return this.getAttributeStringValue(BCStrictStyle.L);
    }

    @Override
    public void setLocality(@Nullable String locality) {
        this.setAttributeStringValue(BCStrictStyle.L, locality);
    }

    @Override
    public boolean hasOrganization() {
        return this.attrs.containsKey(BCStrictStyle.O);
    }

    @Nullable
    @Override
    public String getOrganization() {
        return this.getAttributeStringValue(BCStrictStyle.O);
    }

    @Override
    public void setOrganization(@Nullable String org) {
        this.setAttributeStringValue(BCStrictStyle.O, org);
    }

    @Override
    public boolean hasOrganizationUnit() {
        return this.attrs.containsKey(BCStrictStyle.OU);
    }

    @Nullable
    @Override
    public String getOrganizationUnit() {
        return this.getAttributeStringValue(BCStrictStyle.OU);
    }

    @Override
    public void setOrganizationUnit(@Nullable String orgUnit) {
        this.setAttributeStringValue(BCStrictStyle.OU, orgUnit);
    }

    @Override
    public boolean hasState() {
        return this.attrs.containsKey(BCStrictStyle.ST);
    }

    @Nullable
    @Override
    public String getState() {
        return this.getAttributeStringValue(BCStrictStyle.ST);
    }

    @Override
    public void setState(@Nullable String state) {
        this.setAttributeStringValue(BCStrictStyle.ST, state);
    }
}
