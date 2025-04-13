package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.parser.UsageDefinition;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import java.lang.IllegalArgumentException;
import io.cdap.wrangler.api.DirectiveExecutionException;

import java.util.List;
import java.util.Collections;

public class AggregateStats implements Directive {
    private String sizeColumn;
    private String timeColumn;
    private String outSizeColumn;
    private String outTimeColumn;

    private long totalBytes = 0;
    private long totalMillis = 0;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder("aggregate-stats")
            .withRequiredArg("size_column")
            .withRequiredArg("time_column")
            .withRequiredArg("out_size_column")
            .withRequiredArg("out_time_column")
            .build();
    }



    @Override
    public void initialize(Arguments args) {
        this.sizeColumn = args.value("size_column");
        this.timeColumn = args.value("time_column");
        this.outSizeColumn = args.value("out_size_column");
        this.outTimeColumn = args.value("out_time_column");
    }


    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        for (Row row : rows) {
            Object sizeObj = row.getValue(sizeColumn);
            Object timeObj = row.getValue(timeColumn);

            if (sizeObj != null && timeObj != null) {
                String sizeStr = sizeObj.toString();
                String timeStr = timeObj.toString();

                long sizeBytes = new ByteSize(sizeStr).getBytes();
                long timeMillis = new TimeDuration(timeStr).getMilliseconds();

                totalBytes += sizeBytes;
                totalMillis += timeMillis;
                rowCount++;
            }
        }

        Row output = new Row();
        output.add(outSizeColumn, totalBytes / (1024.0 * 1024.0)); // MB
        output.add(outTimeColumn, totalMillis / 1000.0); // seconds

        return Collections.singletonList(output);
    }

    @Override
    public void destroy() {
    }
}
