package be.atc.salesmanagercrm.beans;

import be.atc.salesmanagercrm.entities.VoucherHistoriesEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class VoucherHistoriesBeanTest {

    @Getter
    @Setter
    private VoucherHistoriesBean voucherHistoriesBean;

    @BeforeEach
    public void init() {
        log.info("Appel avant chaque test");
        voucherHistoriesBean = new VoucherHistoriesBean();
    }

    @AfterEach
    public void after() {
        log.info("Appel après chaque test");
        voucherHistoriesBean = null;
    }

    @Test
    void findAllByIdUserAndByIdTransactionShouldReturnTrue() {

        // Mettre un id incorrect
        int idVoucher = 3;
        int idUser = 1;

        List<VoucherHistoriesEntity> voucherHistoriesEntities = voucherHistoriesBean.findAllByIdUserAndByIdVoucher(idVoucher, idUser);


        boolean test = !voucherHistoriesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(true);
    }

    @Test
    void findAllByIdUserAndByIdTransactionShouldReturnFalse() {

        // Mettre un id correct
        int idVoucher = 1540;
        int idUser = 1;

        List<VoucherHistoriesEntity> voucherHistoriesEntities = voucherHistoriesBean.findAllByIdUserAndByIdVoucher(idVoucher, idUser);


        boolean test = !voucherHistoriesEntities.isEmpty();

        log.info("Le test vaut : " + test);

        assertThat(test).isEqualTo(false);
    }
}