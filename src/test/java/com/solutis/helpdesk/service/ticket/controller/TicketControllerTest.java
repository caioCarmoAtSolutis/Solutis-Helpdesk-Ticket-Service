package com.solutis.helpdesk.service.ticket.controller;

import com.solutis.helpdesk.service.ticket.domain.dto.*;
import com.solutis.helpdesk.service.ticket.domain.model.Category;
import com.solutis.helpdesk.service.ticket.domain.model.Priority;
import com.solutis.helpdesk.service.ticket.domain.model.Status;
import com.solutis.helpdesk.service.ticket.feign.client.user.Role;
import com.solutis.helpdesk.service.ticket.feign.client.user.RoleData;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserData;
import com.solutis.helpdesk.service.ticket.feign.client.user.UserServiceClient;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.ExceptionMessage;
import com.solutis.helpdesk.service.ticket.infrastructure.exception.ExceptionMessageBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional // Automatically rolls back database changes made by MockMvc after each test method
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<TicketData> ticketDataJson;

    @Autowired
    private JacksonTester<TicketPriorityData> ticketPriorityDataJson;

    @Autowired
    private JacksonTester<TicketStatusData> ticketStatusDataJson;

    @Autowired
    private JacksonTester<TicketCategoryData> ticketCategoryDataJson;

    @Autowired
    private JacksonTester<AssignTechnicianData> assignTechnicianDataJson;

    @Autowired
    private JacksonTester<ExceptionMessage> exceptionMessageJson;

    @MockitoBean
    private UserServiceClient userServiceClient;

    private final ExceptionMessageBuilder messages = ExceptionMessageBuilder.getInstance();

    // ---------- UserServiceClient mocking helpers ----------

    private UserData activeUser(UUID id, Role role) {
        return new UserData(id, "Test User", "test_user_" + id + "@gmail.com", new RoleData(role), true, LocalDateTime.now());
    }

    private UserData inactiveUser(UUID id, Role role) {
        return new UserData(id, "Inactive User", "inactive_user_" + id + "@gmail.com", new RoleData(role), false, LocalDateTime.now());
    }

    private void mockUser(UUID id, UserData userData) {
        when(userServiceClient.getUserById(id)).thenReturn(ResponseEntity.ok(userData));
    }

    private void mockUserNotFound(UUID id) {
        when(userServiceClient.getUserById(id)).thenReturn(ResponseEntity.badRequest().build());
    }

    private UUID mockActiveCustomer() {
        UUID id = UUID.randomUUID();
        mockUser(id, activeUser(id, Role.CLIENT));
        return id;
    }

    private UUID mockActiveTechnician() {
        UUID id = UUID.randomUUID();
        mockUser(id, activeUser(id, Role.TECHNICIAN));
        return id;
    }

    // ---------- Exception body helper ----------

    private void assertExceptionMessage(MockHttpServletResponse response, String expectedMessage) throws Exception {
        ExceptionMessage body = exceptionMessageJson.parseObject(response.getContentAsString());
        assertThat(body.message()).isEqualTo(expectedMessage);
    }

    // ---------- Ticket creation helper ----------

    private UUID createTestTicket(UUID customerId) throws Exception {
        TicketData data = new TicketData(
                customerId,
                "Test Ticket",
                "Test ticket description",
                new TicketCategoryData(Category.HARDWARE),
                new TicketPriorityData(Priority.LOW));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.getHeader("Location");
        assertThat(location).isNotNull();
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    // ---------- Create Ticket ----------

    @Test
    @DisplayName("Create ticket with valid data: ResponseEntity.status = 201, Created")
    void createTicketWithValidData() throws Exception {
        UUID customerId = mockActiveCustomer();
        TicketData data = new TicketData(
                customerId,
                "Printer not working",
                "The printer on the 3rd floor is not turning on",
                new TicketCategoryData(Category.HARDWARE),
                new TicketPriorityData(Priority.HIGH));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains("Printer not working");
        assertThat(response.getContentAsString()).contains(customerId.toString());
        assertThat(response.getContentAsString()).contains(Category.HARDWARE.toString());
        assertThat(response.getContentAsString()).contains(Priority.HIGH.toString());
        assertThat(response.getContentAsString()).contains(Status.OPEN.toString());
        assertThat(response.getHeader("Location")).contains("/tickets/");
    }

    @Test
    @DisplayName("Create ticket with non-existent customer id: ResponseEntity.status = 400, Bad Request")
    void createTicketWithNonExistentCustomerReturnsBadRequest() throws Exception {
        UUID nonExistentCustomerId = UUID.randomUUID();
        mockUserNotFound(nonExistentCustomerId);

        TicketData data = new TicketData(
                nonExistentCustomerId,
                "Some issue",
                "Some description",
                new TicketCategoryData(Category.SOFTWARE),
                new TicketPriorityData(Priority.MEDIUM));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userIdNotFountMessage(nonExistentCustomerId.toString()));
    }

    @Test
    @DisplayName("Create ticket with inactive customer: ResponseEntity.status = 400, Bad Request")
    void createTicketWithInactiveCustomerReturnsBadRequest() throws Exception {
        UUID customerId = UUID.randomUUID();
        mockUser(customerId, inactiveUser(customerId, Role.CLIENT));

        TicketData data = new TicketData(
                customerId,
                "Some issue",
                "Some description",
                new TicketCategoryData(Category.SOFTWARE),
                new TicketPriorityData(Priority.MEDIUM));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userIsInactiveMessage(customerId.toString()));
    }

    @Test
    @DisplayName("Create ticket with customer id that has the wrong role: ResponseEntity.status = 400, Bad Request")
    void createTicketWithCustomerHavingWrongRoleReturnsBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UserData technicianAsCustomer = activeUser(userId, Role.TECHNICIAN);
        mockUser(userId, technicianAsCustomer);

        TicketData data = new TicketData(
                userId,
                "Some issue",
                "Some description",
                new TicketCategoryData(Category.SOFTWARE),
                new TicketPriorityData(Priority.MEDIUM));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userHasInvalidRoleMessage(technicianAsCustomer, Role.CLIENT));
    }

    @Test
    @DisplayName("Create ticket with blank title: ResponseEntity.status = 400, Bad Request")
    void createTicketWithBlankTitleReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        TicketData data = new TicketData(
                customerId,
                "",
                "Some description",
                new TicketCategoryData(Category.SOFTWARE),
                new TicketPriorityData(Priority.MEDIUM));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Create ticket with description exceeding max size: ResponseEntity.status = 400, Bad Request")
    void createTicketWithDescriptionTooLongReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        TicketData data = new TicketData(
                customerId,
                "Valid title",
                "a".repeat(251),
                new TicketCategoryData(Category.SOFTWARE),
                new TicketPriorityData(Priority.MEDIUM));

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // ---------- Get Ticket By Id ----------

    @Test
    @DisplayName("Get ticket by id when ticket exists: ResponseEntity.status = 200, OK")
    void getTicketByIdWhenTicketExistsReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ticketId.toString());
        assertThat(response.getContentAsString()).contains(customerId.toString());
    }

    @Test
    @DisplayName("Get ticket by id when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void getTicketByIdWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }

    // ---------- Get All Tickets ----------

    @Test
    @DisplayName("Get all tickets: ResponseEntity.status = 200, OK")
    void getAllTicketsReturnsOkAndPaginatedList() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ticketId.toString());
        assertThat(response.getContentAsString()).contains("content");
        assertThat(response.getContentAsString()).contains("pageable");
    }

    @Test
    @DisplayName("Get all tickets with custom pageable: ResponseEntity.status = 200, OK")
    void getAllTicketsWithCustomPageableReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"size\":5");
    }

    // ---------- Get Tickets By Customer Id ----------

    @Test
    @DisplayName("Get tickets by customer id when customer is valid: ResponseEntity.status = 200, OK")
    void getTicketsBySpecificCustomerIdReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets/customer/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ticketId.toString());
    }

    @Test
    @DisplayName("Get tickets by customer id when customer id is not valid: ResponseEntity.status = 400, Bad Request")
    void getTicketsBySpecificCustomerIdWhenCustomerInvalidReturnsBadRequest() throws Exception {
        UUID nonExistentCustomerId = UUID.randomUUID();
        mockUserNotFound(nonExistentCustomerId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/tickets/customer/{customerId}", nonExistentCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userIdNotFountMessage(nonExistentCustomerId.toString()));
    }

    // ---------- Change Ticket Priority ----------

    @Test
    @DisplayName("Change ticket priority with valid data: ResponseEntity.status = 200, OK")
    void changeTicketPriorityWithValidDataReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        TicketPriorityData data = new TicketPriorityData(Priority.CRITICAL);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/priority", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketPriorityDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(Priority.CRITICAL.toString());
    }

    @Test
    @DisplayName("Change ticket priority when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void changeTicketPriorityWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        TicketPriorityData data = new TicketPriorityData(Priority.HIGH);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/priority", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketPriorityDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }

    @Test
    @DisplayName("Change ticket priority with null priority: ResponseEntity.status = 400, Bad Request")
    void changeTicketPriorityWithNullPriorityReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/priority", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"priority\": null}"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // ---------- Change Ticket Status ----------

    @Test
    @DisplayName("Change ticket status with valid data: ResponseEntity.status = 200, OK")
    void changeTicketStatusWithValidDataReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        TicketStatusData data = new TicketStatusData(Status.IN_PROGRESS);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketStatusDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(Status.IN_PROGRESS.toString());
    }

    @Test
    @DisplayName("Change ticket status when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void changeTicketStatusWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        TicketStatusData data = new TicketStatusData(Status.RESOLVED);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/status", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketStatusDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }

    // ---------- Change Ticket Category ----------

    @Test
    @DisplayName("Change ticket category with valid data: ResponseEntity.status = 200, OK")
    void changeTicketCategoryWithValidDataReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        TicketCategoryData data = new TicketCategoryData(Category.NETWORK);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/category", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketCategoryDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(Category.NETWORK.toString());
    }

    @Test
    @DisplayName("Change ticket category when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void changeTicketCategoryWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        TicketCategoryData data = new TicketCategoryData(Category.SOFTWARE);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/category", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketCategoryDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }

    // ---------- Assign Technician ----------

    @Test
    @DisplayName("Assign technician with valid data: ResponseEntity.status = 200, OK")
    void assignTechnicianWithValidDataReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);
        UUID technicianId = mockActiveTechnician();

        AssignTechnicianData data = new AssignTechnicianData(technicianId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/technician", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignTechnicianDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(technicianId.toString());
    }

    @Test
    @DisplayName("Assign technician when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void assignTechnicianWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentTicketId = UUID.randomUUID();
        UUID technicianId = mockActiveTechnician();

        AssignTechnicianData data = new AssignTechnicianData(technicianId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/technician", nonExistentTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignTechnicianDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentTicketId.toString()));
    }

    @Test
    @DisplayName("Assign technician when technician id does not exist: ResponseEntity.status = 400, Bad Request")
    void assignTechnicianWhenTechnicianDoesNotExistReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);
        UUID nonExistentTechnicianId = UUID.randomUUID();
        mockUserNotFound(nonExistentTechnicianId);

        AssignTechnicianData data = new AssignTechnicianData(nonExistentTechnicianId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/technician", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignTechnicianDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userIdNotFountMessage(nonExistentTechnicianId.toString()));
    }

    @Test
    @DisplayName("Assign technician when technician is inactive: ResponseEntity.status = 400, Bad Request")
    void assignTechnicianWhenTechnicianInactiveReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);
        UUID technicianId = UUID.randomUUID();
        mockUser(technicianId, inactiveUser(technicianId, Role.TECHNICIAN));

        AssignTechnicianData data = new AssignTechnicianData(technicianId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/technician", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignTechnicianDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userIsInactiveMessage(technicianId.toString()));
    }

    @Test
    @DisplayName("Assign technician when user has wrong role: ResponseEntity.status = 400, Bad Request")
    void assignTechnicianWhenUserHasWrongRoleReturnsBadRequest() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);
        UUID wrongRoleUserId = UUID.randomUUID();
        UserData clientAsTechnician = activeUser(wrongRoleUserId, Role.CLIENT);
        mockUser(wrongRoleUserId, clientAsTechnician);

        AssignTechnicianData data = new AssignTechnicianData(wrongRoleUserId);

        MockHttpServletResponse response = mockMvc.perform(
                patch("/tickets/{id}/technician", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignTechnicianDataJson.write(data).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.userHasInvalidRoleMessage(clientAsTechnician, Role.TECHNICIAN));
    }

    // ---------- Close Ticket ----------

    @Test
    @DisplayName("Close ticket when ticket exists: ResponseEntity.status = 200, OK")
    void closeTicketWhenTicketExistsReturnsOk() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets/{id}/close", ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(ticketId.toString());
        assertThat(response.getContentAsString()).contains(Status.CLOSED.toString());
    }

    @Test
    @DisplayName("Close ticket when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void closeTicketWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        MockHttpServletResponse response = mockMvc.perform(
                post("/tickets/{id}/close", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }

    // ---------- Delete Ticket ----------

    @Test
    @DisplayName("Delete ticket when ticket exists: ResponseEntity.status = 204, No Content")
    void deleteTicketWhenTicketExistsReturnsNoContent() throws Exception {
        UUID customerId = mockActiveCustomer();
        UUID ticketId = createTestTicket(customerId);

        MockHttpServletResponse response = mockMvc.perform(
                delete("/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        MockHttpServletResponse getResponse = mockMvc.perform(
                get("/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Delete ticket when ticket does not exist: ResponseEntity.status = 400, Bad Request")
    void deleteTicketWhenTicketDoesNotExistReturnsBadRequest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        MockHttpServletResponse response = mockMvc.perform(
                delete("/tickets/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertExceptionMessage(response, messages.ticketIdNotFountMessage(nonExistentId.toString()));
    }
}