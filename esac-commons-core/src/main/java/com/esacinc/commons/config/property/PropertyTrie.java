package com.esacinc.commons.config.property;

import java.util.NavigableMap;
import java.util.NavigableSet;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Trie;

public interface PropertyTrie<T> extends NavigableMap<String, T>, Trie<String, T> {
    @Override
    public NavigableMap<String, T> prefixMap(String key);

    public boolean containsLeafKey(String key);

    public boolean containsBranchKey(String key);

    public boolean containsNextKey(String key);

    @Nullable
    @Override
    public String nextKey(String key);

    public boolean containsPreviousKey(String key);

    @Nullable
    @Override
    public String previousKey(String key);

    public boolean containsHigherKey(String key);

    public boolean containsLowerKey(String key);

    public NavigableSet<String> getRootKeys();
}
