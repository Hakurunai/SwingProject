package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.util.LoggerHelper;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * A model shared between class who need to work with the different value of an {@link Order}
 */
public abstract class OrderDialog extends DataDialog<Order>
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask   A {@link Consumer} who can be called by any child class if necessary
     */
    public OrderDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                       Consumer<OperationResult<Order>> onDialogAchieveTask)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
    }

    @Override
    protected void Init()
    {
        LoggerHelper.log.info("START : initialisation of an OrderDialog window");
    }
}
