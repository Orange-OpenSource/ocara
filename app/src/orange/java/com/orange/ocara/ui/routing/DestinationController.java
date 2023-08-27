package com.orange.ocara.ui.routing;

import android.content.Context;
import android.content.Intent;

import com.orange.ocara.ui.activity.CreateAuditActivityOrange_;
import com.orange.ocara.ui.activity.CreateSiteActivityOrange_;
import com.orange.ocara.ui.activity.UpdateAuditActivityOrange_;
import com.orange.ocara.ui.routing.Navigation;

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

        CreateAuditActivityOrange_
                .intent(context)
                .auditId(null)
                .start();
    }

    @Override
    public void navigateToHome(Context context, int resultCode) {

        CreateAuditActivityOrange_
                .intent(context)
                .auditId(null)
                .startForResult(resultCode);
    }

    @Override
    public void navigateToAuditEditView(Context context, long auditId, int resultCode) {
        UpdateAuditActivityOrange_
                .intent(context)
                .auditId(auditId)
                .startForResult(resultCode);
    }

    @Override
    public void navigateToSiteCreateView(Context context, int resultCode) {
        CreateSiteActivityOrange_
                .intent(context)
                .startForResult(resultCode);
    }

    @Override
    public void terminate(Context context) {
        Intent intent = new Intent(context, CreateAuditActivityOrange_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("disconnect", true);
        context.startActivity(intent);
    }
}
