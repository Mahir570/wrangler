/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.directives.custom;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {
  @Test
  public void testAggregateStatsTotal() throws Exception {
      List<Row> rows = Arrays.asList(
          new Row().add("data_transfer_size", 1024L).add("response_time", 1000L),
          new Row().add("data_transfer_size", 2048L).add("response_time", 2000L),
          new Row().add("data_transfer_size", 3072L).add("response_time", 3000L)
      );
  
      String[] recipe = {
        "aggregate-stats :data_transfer_size :response_time :total_size_mb :total_time_sec 'MB' 's' 'total'"
      };
      
      
  
      List<Row> result = TestingRig.execute(recipe, rows);
  
      Assert.assertEquals(1, result.size());
      
      // Calculate expected values
      long totalBytes = 1024L + 2048L + 3072L; // 6144 bytes
      double expectedSizeMB = totalBytes / (1024.0 * 1024); // ~0.005859375 MB
      
      long totalMillis = 1000L + 2000L + 3000L; // 6000 ms
      double expectedTimeSec = totalMillis / 1000.0; // 6.0 seconds
      
      Assert.assertEquals(expectedSizeMB, 
                         ((Number) result.get(0).getValue("total_size_mb")).doubleValue(), 
                         0.000001);
      Assert.assertEquals(expectedTimeSec,
                         ((Number) result.get(0).getValue("total_time_sec")).doubleValue(),
                         0.000001);
  }
}
