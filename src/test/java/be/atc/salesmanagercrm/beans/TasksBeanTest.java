package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.enums.EnumPriority;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class TasksBeanTest {

    @Test
    void save() {

        boolean test = Arrays.stream(EnumPriority.values())
                .map(EnumPriority::getLabel)
                .collect(Collectors.toSet())
                .contains("Moyenne");


        assertThat(test).isEqualTo(true);

    }
}