package com.dias.services.mnemo.model;

import lombok.*;

/**
 * Мнемо схема
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schema extends AbstractModel {
    private String title;
    private String createdBy;
    private String content;
}
