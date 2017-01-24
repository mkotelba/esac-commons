package com.esacinc.commons.crypto.naming;

import com.google.common.collect.ListMultimap;
import java.util.Comparator;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;

public interface Dn {
    public X500Name toX500Name();

    public X500Name toX500Name(@Nullable Comparator<ASN1ObjectIdentifier> attrOidComparator);
    
    public ListMultimap<ASN1ObjectIdentifier, ASN1Encodable> getAttributes();

    public boolean hasCommonName();

    @Nullable
    public String getCommonName();

    public void setCommonName(@Nullable String commonName);

    public boolean hasCountry();

    @Nullable
    public String getCountry();

    public void setCountry(@Nullable String country);

    public boolean hasLocality();

    @Nullable
    public String getLocality();

    public void setLocality(@Nullable String locality);

    public boolean hasOrganization();

    @Nullable
    public String getOrganization();

    public void setOrganization(@Nullable String org);

    public boolean hasOrganizationUnit();

    @Nullable
    public String getOrganizationUnit();

    public void setOrganizationUnit(@Nullable String orgUnit);

    public boolean hasState();

    @Nullable
    public String getState();

    public void setState(@Nullable String state);
}
