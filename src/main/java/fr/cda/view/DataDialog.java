package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.OperationResult;
import fr.cda.view.swing.SwingDialog;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * Model for Dialog who need to create or update some data for another window
 */
public abstract class DataDialog <T> extends SwingDialog
{
    protected final Consumer<OperationResult<T>> OnDialogAchieveTask;

    protected JButton validateButton;
    protected JButton cancelButton;

    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param achieveTaskCallback   A {@link Consumer} who can be called by any child class if necessary
     */
    public DataDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner, final Consumer<OperationResult<T>> achieveTaskCallback)
    {
        super(config, controller, frameOwner, true);
        this.OnDialogAchieveTask = achieveTaskCallback;
    }

    /**
     * Method called when the button Validate is pressed
     */
    protected abstract void OnValidateButton();

    /**
     * Initialize validate and cancel button
     */
    @Override
    protected void Init()
    {
        validateButton = new JButton("Validate");
        cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(e -> Close());
        validateButton.addActionListener(e -> OnValidateButton());
    }

    /**
     * Call to end this Dialog and call the callback given to her at her creation on the result of their work
     * @param taskOperationRes A wrapper of the result from the {@link fr.cda.model.Database}
     * @param toReturn The Product given to the callback {{@link #OnDialogAchieveTask}}
     */
    protected void DialogAchieveTask(final OperationResult<Void> taskOperationRes, final T toReturn)
    {
        if (taskOperationRes.HasSucceeded())
        {
            OnDialogAchieveTask.accept(OperationResult.SUCCESS(toReturn, taskOperationRes.getMessage()));
            Close();
        }
        else
        {
            JOptionPane.showMessageDialog(dialog, taskOperationRes.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
