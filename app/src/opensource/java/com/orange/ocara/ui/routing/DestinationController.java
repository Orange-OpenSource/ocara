package com.orange.ocara.ui.routing;

import android.content.Context;

import com.orange.ocara.ui.activity.CreateAuditActivity_;
import com.orange.ocara.ui.activity.CreateSiteActivity_;
import com.orange.ocara.ui.activity.UpdateAuditActivity_;

import org.androidannotations.annotations.EBean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * UI Routing Configuration
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class DestinationController implements Navigation {

    @Override
    public void navigateToHome(Context context) {
        CreateAuditActivity_
                .intent(context)
                .auditId(null)
                .start();
    }

    @Override
    public void navigateToHome(Context context, int resultCode) {

        CreateAuditActivity_
                .intent(context)
                .auditId(null)
                .startForResult(resultCode);
    }

    @Override
    public void navigateToAuditEditView(Context context, long auditId, int resultCode) {
        UpdateAuditActivity_
                .intent(context)
                .auditId(auditId)
                .startForResult(resultCode);
    }

    @Override
    public void navigateToSiteCreateView(Context context, int resultCode) {
        CreateSiteActivity_
                .intent(context)
                .startForResult(resultCode);
    }

    @Override
    public void terminate(Context context) {

        // do nothing yet
    }
}
