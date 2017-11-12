package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;

public class BasicHealthMetricsTest {

    @Test
    public void testBasicHealthMetrics () {
        final BasicHealthMetrics metrics = new BasicHealthMetrics();
        metrics.hashCode();
        final BasicHealthMetrics metrics1 = new BasicHealthMetrics();
        assertTrue( metrics.equals( metrics1 ) );
    }
}
