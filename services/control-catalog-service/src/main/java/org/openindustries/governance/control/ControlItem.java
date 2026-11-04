package org.openindustries.governance.control;

import java.util.UUID;

/** ControlItem aggregate — Catalogue of governance controls and their review cycles. */
public class ControlItem {
    public UUID id;
        public String name;
        public String controlType;
        public int reviewCycleDays;
    public String status;

    public static ControlItem create(String name, String controlType, int reviewCycleDays) {
        ControlItem e = new ControlItem();
        e.id = UUID.randomUUID();
        e.name = name;
        e.controlType = controlType;
        e.reviewCycleDays = reviewCycleDays;
        e.status = "ACTIVE";
        return e;
    }
}
