package com.izaiasvalentim.general.Common.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static Instant getCurrentTimeTruncatedInSeconds() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
