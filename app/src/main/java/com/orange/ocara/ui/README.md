# Presentation layer

Presentation layer interacts with the UI. It contains of Android UI elements (activities, fragments, views) and presenters or view models, depending on the presentation pattern you decide to use.

**Presentation Layer depends on Domain Layer.**

The design pattern of this layer that is implementd is named *Model-View-Presenter*.

## MVP Design Pattern
This pattern divides an application into three major aspects: Model, View, and Presenter.

### Model
The Model represents a set of classes that describes the business logic and data. It also defines business rules for data means how the data can be changed and manipulated.

### View
View is a component which is directly interacts with user like XML, Activity, fragments. It does not contain any logic implemented.

### Presenter
The Presenter receives the input from users via View, then process the user’s data with the help of Model and passing the results back to the View. Presenter communicates with view through interface. Interface is defined in presenter class, to which it pass the required data. Activity/fragment or any other view component implement this interface and renders the data in a way they want.

In the MVP design pattern, the presenter manipulates the model and also updates the view. In MVP View and Presenter are completely decoupled from each other’s and communicate to each other’s by an interface. Because if decoupling mocking of the view is easier and unit testing of applications that leverage the MVP design pattern over the MVC design pattern are much easier.

## How MVP is implemented in OCARA

### The contract
An interface defines the contract between the view and the user's actions listener. Then an Activity/Fragment shall implement the **View** interface, and a Presenter the related **Listener** interface.

```java

/**
 * Contract between the view and the presenter, that deal with the listing of equipments / object descriptions
 */
public interface ListEquipmentsContract {

    /**
     * Behaviour of the view
     */
    interface ListEquipmentsView {

        /**
         * displays the items
         * @param equipments a bunch of {@link Equipment}s
         */
        void showEquipments(List<Equipment> equipments);

        /**
         * displays an information
         */
        void showNoEquipments();
    }

    /**
     * behaviour of the presenter
     */
    interface ListEquipmentsUserActionsListener {

        /**
         * requests for data
         *
         * @param rulesetId an identifier
         */
        void loadAllEquipmentsByRulesetId(Long rulesetId);
    }
}
```

### The View Layer, named UI

First option, we can create the presenter directly in the Activity :

```java
@EFragment(...)
public class EquipmentsByRulesetDisplayFragment extends BaseFragment 
      implements ListEquipmentsContract.ListEquipmentsView, ListQuestionsContract.ListQuestionsView {
    private ListQuestionsContract.ListQuestionsUserActionsListener questionsActionListener;

    private ListEquipmentsContract.ListEquipmentsUserActionsListener equipmentsActionsListener;

    @AfterViews
    void initData() {
        equipmentsActionsListener = new ListEquipmentsPresenter(equipmentService, this);

        questionsActionListener = new ListQuestionsPresenter(questionService, this);
    }
}
```

Or, second option, we can offer a function in a dedicated UiConfig class, and inject this config file in the Activity/Fragment. This way, the config class is the only element that is injected in the activity, and the activity does not have to manage the configuration and the instantiation of the components.

```java
/**
 * Configuration for the UI layer of the Terms-Of-Use module
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EBean(scope = EBean.Scope.Singleton)
public class TermsAcceptanceUiConfig {

    @Bean(BizConfig.class)
    BizConfig bizConfig;

    /**
     * Retrieves a listener for actions, according to {@link TermsOfUseAcceptanceContract}
     *
     * @param view a {@link TermsOfUseAcceptanceView}
     * @return an instance of {@link TermsOfUseAcceptanceUserActionsListener}
     */
    public TermsOfUseAcceptanceUserActionsListener actionsListener(TermsOfUseAcceptanceView view) {
        return new TermsOfUseAcceptancePresenter(
                view,
                bizConfig.acceptTermsOfUseTask(),
                bizConfig.declineTermsOfUseTask(),
                bizConfig.loadTermsOfUseTask());
    }
}

@EActivity(...)
public class TermsOfUseAcceptanceFragment extends FragmentActivity implements TermsOfUseAcceptanceView {

    /**
     * the actions that can be executed by a user
     */
    private TermsOfUseAcceptanceUserActionsListener actionsListener;

    /**
     * the UI config
     */
    @Bean(TermsAcceptanceUiConfig.class)
    TermsAcceptanceUiConfig uiConfig;

    /**
     * Init function
     */
    @AfterViews
    @Background
    public void afterViews() {

        actionsListener = uiConfig.actionsListener(this);

        actionsListener.loadTerms();
    }
}
```
