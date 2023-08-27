package com.orange.ocara.domain.interactors;

import com.orange.ocara.domain.models.AuditModel;
import com.orange.ocara.domain.repositories.AuditRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateAuditTask {

    private final AuditRepository auditRepository;

    @Inject
    public UpdateAuditTask(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public Completable execute(AuditModel auditModel){
       return auditRepository.updateAudit(auditModel);
    }
}
