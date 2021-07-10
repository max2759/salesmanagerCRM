/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.barcelona.view.data.datatable;

import be.atc.salesmanagercrm.beans.JobTitlesBean;
import be.atc.salesmanagercrm.entities.JobTitlesEntity;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.barcelona.domain.Car;
import org.primefaces.barcelona.service.CarService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("dtPaginatorView")
@ViewScoped
public class PaginatorView implements Serializable {

    private List<Car> cars;

    @Getter
    @Setter
    private List<JobTitlesEntity> jobTitlesEntities;

    @Inject
    private CarService service;

    @Inject
    private JobTitlesBean jobTitlesBean;

    @PostConstruct
    public void init() {
        cars = service.createCars(50);
    }

    public void getJobEntities() {
        jobTitlesEntities = jobTitlesBean.findAllJobTitles();
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setService(CarService service) {
        this.service = service;
    }
}
