package net.deali.intern.infrastructure.configuration;

import net.deali.intern.infrastructure.util.BatchScheduledJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class QuartzConfiguration {
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(cronSchedule("0 * * * * ?"))
                .build();
    }
}
