 package io.cdap.directives.aggregates;

 import io.cdap.wrangler.TestingRig;
 import io.cdap.wrangler.api.Row;
 import org.junit.Test;
 import org.junit.Assert;
 
 import java.util.ArrayList;
 import java.util.List;
 
 /**
  * Tests {@link AggregateStats}
  */
 public class AggregateStatsTest {
 
   @Test
   public void testAggregateStatsDirective() throws Exception {
     // Define the recipe
     String[] recipe = new String[] {
       "aggregate-stats :data_transfer :duration total_mb total_sec"
     };
 
     // Sample input rows
     List<Row> rows = new ArrayList<>();
     rows.add(new Row("data_transfer", "1MB").add("duration", "1s"));     // 1MB, 1s
     rows.add(new Row("data_transfer", "2MB").add("duration", "500ms")); // 2MB, 0.5s
     rows.add(new Row("data_transfer", "512KB").add("duration", "250ms")); // 0.5MB, 0.25s
 
     // Expected total: 3.5MB and 1.75s
     List<Row> result = TestingRig.execute(recipe, rows);
 
     Assert.assertEquals(1, result.size());
     Row output = result.get(0);
 
     double totalMB = ((Number) output.getValue("total_mb")).doubleValue();
     double totalSec = ((Number) output.getValue("total_sec")).doubleValue();
 
     Assert.assertEquals(3.5, totalMB, 0.001);
     Assert.assertEquals(1.75, totalSec, 0.001);
   }
 }
 