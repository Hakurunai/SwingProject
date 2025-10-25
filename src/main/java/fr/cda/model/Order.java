package fr.cda.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    private String id;
    private LocalDate creationDate;
    private String clientName;
    private List<Product> orderedProduct = new ArrayList<Product>();

    private boolean isDelivered = false;
    private String nonDeliveredExplanation;
}
