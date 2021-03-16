package net.zoostar.hw.job;

import org.springframework.batch.item.data.GemfireItemWriter;

import net.zoostar.hw.model.Person;

public class GemfireGridWriter extends GemfireItemWriter<String, Person> {

}
