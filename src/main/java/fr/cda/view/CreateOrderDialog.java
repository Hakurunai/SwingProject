package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

public class CreateOrderDialog extends OrderDialog
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask   A {@link Consumer} who can be called by any child class if necessary
     */
    public CreateOrderDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner, Consumer<OperationResult<Order>> onDialogAchieveTask)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
    }

    @Override
    protected void OnValidateButton()
    {
        System.out.println("VALIDATE !!!\n");
    }


}
