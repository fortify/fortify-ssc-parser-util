/**
 * Copyright 2023 Open Text.
 *
 * The only warranties for products and services of Open Text 
 * and its affiliates and licensors (“Open Text”) are as may 
 * be set forth in the express warranty statements accompanying 
 * such products and services. Nothing herein should be construed 
 * as constituting an additional warranty. Open Text shall not be 
 * liable for technical or editorial errors or omissions contained 
 * herein. The information contained herein is subject to change 
 * without notice.
 */
package com.fortify.util.ssc.parser;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanEntry;

/**
 * This helper class provides methods for retrieving {@link ScanEntry}
 * instances based on predicates.
 * 
 * @author Ruud Senden
 */
public final class ScanEntryHelper {
    public static final ScanEntry getScanEntry(ScanData scanData, Predicate<ScanEntry> predicate) {
        List<ScanEntry> supportedEntries = scanData.getScanEntries().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        switch ( supportedEntries.size() ) {
        case 0: throw new IllegalStateException("No matching entry found");
        case 1: return supportedEntries.get(0);
        default: throw new IllegalStateException("multiple matching entries found");
        }
    }

    public static final ScanEntry getScanEntryByName(ScanData scanData, Predicate<String> predicate) {
        return getScanEntry(scanData, entry->predicate.test(entry.getEntryName()));
    }
}
