package com.solutis.helpdesk.service.ticket.service;

import com.solutis.helpdesk.service.ticket.domain.dto.TicketCategoryData;
import com.solutis.helpdesk.service.ticket.domain.dto.TicketData;
import com.solutis.helpdesk.service.ticket.domain.dto.TicketPriorityData;
import com.solutis.helpdesk.service.ticket.domain.model.Category;
import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoadDatabaseService {
    @Autowired
    private TicketService ticketService;

    public void load() {
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("e2a0f821-6b4d-4b8a-921d-78a2e12a0f10"),
                        "Unable to reset password via email",
                        "I clicked on the reset password link, but no email arrives in my inbox or spam folder.",
                        Category.SOFTWARE,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("a8c9e0d1-3b2a-4c5d-8e7f-6a5b4c3d2e1f"),
                        "Payment gateway timeout during checkout",
                        "Every time I attempt to complete the purchase, the payment step times out after 30 seconds.",
                        Category.SOFTWARE,
                        Priority.CRITICAL));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                        "Dark mode toggle is missing on mobile UI",
                        "The option to enable dark mode exists on desktop, but cannot be found in the mobile web view.",
                        Category.SOFTWARE,
                        Priority.LOW));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("3b2a1c0d-4e5f-6a7b-8c9d-0e1f2a3b4c5d"),
                        "API returns 500 error on batch export",
                        "Executing the /api/v1/export endpoint with more than 100 items causes an internal server error.",
                        Category.SOFTWARE,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("9d8c7b6a-5e4d-3c2b-1a0f-9e8d7c6b5a4f"),
                        "Invoice shows incorrect VAT calculation",
                        "The tax total on invoice #INV-9821 reflects 20% instead of the standard regional 15%.",
                        Category.SOFTWARE,
                        Priority.MEDIUM));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d"),
                        "Request for SSO integration setup",
                        "Our team needs assistance connecting Okta SAML SSO to our enterprise workspace instance.",
                        Category.NETWORK,
                        Priority.MEDIUM));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("6f5e4d3c-2b1a-0f9e-8d7c-6b5a4f3e2d1c"),
                        "Duplicate charges on monthly subscription",
                        "I was billed twice on September 1st for my recurring Pro Tier plan.",
                        Category.SOFTWARE,
                        Priority.CRITICAL));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("5a4b3c2d-1e0f-9a8b-7c6d-5e4f3a2b1c0d"),
                        "Profile picture upload fails with PNG",
                        "Uploading any valid PNG image under 2MB returns an 'Unsupported File Format' alert.",
                        Category.SOFTWARE,
                        Priority.LOW));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("8e7f6a5b-4c3d-2e1f-0a9b-8c7d6e5f4a3b"),
                        "Data synchronization delayed over 2 hours",
                        "Changes made in the cloud portal are taking upwards of two hours to mirror in local sync clients.",
                        Category.NETWORK,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("d4c3b2a1-0e9f-8a7b-6c5d-4e3f2a1b0c9d"),
                        "Feature Request: Export reports to CSV",
                        "We currently only have PDF exports available. Adding CSV export would greatly help our analytics team.",
                        Category.SOFTWARE,
                        Priority.LOW));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("7a8b9c0d-1e2f-3a4b-5c6d-7e8f9a0b1c2d"),
                        "Two-factor authentication code expired error",
                        "TOTP codes generated by Authenticator app immediately report as expired upon submission.",
                        Category.SOFTWARE,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("0f9e8d7c-6b5a-4f3e-2d1c-0b9a8f7e6d5c"),
                        "SSL Certificate warning on subdomain",
                        "Browsers flag secure connections to app.domain.com as untrusted due to an expired SSL certificate.",
                        Category.NETWORK,
                        Priority.CRITICAL));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("2c3d4e5f-6a7b-8c9d-0e1f-2a3b4c5d6e7f"),
                        "Webhook payload missing custom attributes",
                        "The event payload received on user.created hook does not contain metadata fields configured in settings.",
                        Category.SOFTWARE,
                        Priority.MEDIUM));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("b5a4f3e2-d1c0-9b8a-7f6e-5d4c3b2a10fe"),
                        "Dashboard analytics charts not loading",
                        "The main metrics dashboard displays indefinite loading spinners across all widgets.",
                        Category.SOFTWARE,
                        Priority.MEDIUM));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("e1f2a3b4-c5d6-7e8f-9a0b-1c2d3e4f5a6b"),
                        "Credit card update fails without error message",
                        "Submitting new payment card info resets the form silently without confirming or alerting of error.",
                        Category.SOFTWARE,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("4c5d6e7f-8a9b-0c1d-2e3f-4a5b6c7d8e9f"),
                        "Typo in notification email template",
                        "The welcome email contains a typo in the main greeting header: 'Welcom to the platform'.",
                        Category.SOFTWARE,
                        Priority.LOW));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("f9e8d7c6-b5a4-3f2e-1d0c-9b8a7f6e5d4c"),
                        "Zapier integration disconnects daily",
                        "Our active Zapier trigger authorization expires and disconnects every 24 hours automatically.",
                        Category.NETWORK,
                        Priority.HIGH));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d"),
                        "Account deletion request",
                        "In accordance with GDPR, please process the complete deletion of my account and associated personal data.",
                        Category.SOFTWARE,
                        Priority.MEDIUM));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("8f7e6d5c-4b3a-2f1e-0d9c-8b7a6f5e4d3c"),
                        "Bulk email sending feature request",
                        "It would be beneficial to select multiple contacts and send custom broadcast messages directly.",
                        Category.SOFTWARE,
                        Priority.LOW));
        ticketService.createTicket(
                this.mockTicketData(
                        UUID.fromString("1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f"),
                        "Database connection drop during peak hours",
                        "High traffic at 2 PM daily causes frequent 'Connection pool exhausted' errors for active sessions.",
                        Category.HARDWARE,
                        Priority.CRITICAL));
    }

    public TicketData mockTicketData(UUID customerId, String title, String description, Category category, Priority priority) {
        return new TicketData(customerId, title, description, new TicketCategoryData(category), new TicketPriorityData(priority));
    }
}
