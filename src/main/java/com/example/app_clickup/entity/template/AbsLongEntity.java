package com.example.app_clickup.entity.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class AbsLongEntity extends AbsMainEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
