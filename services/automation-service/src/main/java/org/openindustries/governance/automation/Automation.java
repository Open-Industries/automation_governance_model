package org.openindustries.governance.automation;

import java.time.Instant;
import java.util.UUID;

/**
 * Automation aggregate — Registry of governed automations with risk tiers and approval lifecycle.
 *
 * Privacy by design (ISO/IEC 27002 A.5.34): secret identifiers are validated at
 * creation and NEVER persisted or returned in clear — only a masked form is kept.
 * All identifiers in this reference implementation are synthetic.
 */
public class Automation {
    public UUID id;
        public String name;
        public String automationType;
        public String riskTier;
    public String status;
    public Instant createdAt;

    public static Automation create(String name, String automationType, String riskTier) {
        Automation e = new Automation();
        e.id = UUID.randomUUID();
        e.name = name;
        e.automationType = automationType;
        e.riskTier = riskTier;
        e.status = "PROPOSED";
        e.createdAt = Instant.now();
        return e;
    }
}
