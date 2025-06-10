package org.example.expert;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiscTest {
    private Logger logger = LoggerFactory.getLogger(MiscTest.class);

    @Test
    void logNull() {
        logger.info(null);
    }
}
