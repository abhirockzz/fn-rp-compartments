package io.fnproject.example;

import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;
import com.oracle.bmc.identity.IdentityClient;
import com.oracle.bmc.identity.requests.ListCompartmentsRequest;
import com.oracle.bmc.identity.responses.ListCompartmentsResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListCompartmentsFunction {

    private IdentityClient identityClient = null;
    final ResourcePrincipalAuthenticationDetailsProvider provider
            = ResourcePrincipalAuthenticationDetailsProvider.builder().build();

    public ListCompartmentsFunction() {

        //print env vars in Functions container
        System.err.println("OCI_RESOURCE_PRINCIPAL_VERSION " + System.getenv("OCI_RESOURCE_PRINCIPAL_VERSION"));
        System.err.println("OCI_RESOURCE_PRINCIPAL_REGION " + System.getenv("OCI_RESOURCE_PRINCIPAL_REGION"));
        System.err.println("OCI_RESOURCE_PRINCIPAL_RPST " + System.getenv("OCI_RESOURCE_PRINCIPAL_RPST"));
        System.err.println("OCI_RESOURCE_PRINCIPAL_PRIVATE_PEM " + System.getenv("OCI_RESOURCE_PRINCIPAL_PRIVATE_PEM"));

        try {

            identityClient = new IdentityClient(provider);

        } catch (Throwable ex) {
            System.err.println("Failed to instantiate IdentityClient - " + ex.getMessage());
        }
    }

    /**
     * returns compartment and child compartments in the root compartment (the tenancy)
     * 
     * @param tenantOCID tenant (aka root compartment) OCID
     * @return list of compartments including children
     */
    public List<String> handle(String tenantOCID) {

        if (identityClient == null) {
            System.err.println("There was a problem creating the IdentityClient object. Please check logs...");
            return Collections.emptyList();
        }

        List<String> compartmentNames = null;
        try {
            System.err.println("Searching for compartments in tenancy " + tenantOCID);

            ListCompartmentsRequest request = ListCompartmentsRequest.builder().compartmentId(tenantOCID)
                                                                                .compartmentIdInSubtree(Boolean.TRUE)
                                                                                .build();

            ListCompartmentsResponse compartments = identityClient.listCompartments(request);

            compartmentNames = compartments.getItems().stream()
                    .map((compartment) -> compartment.getName())
                    .collect(Collectors.toList());

            System.err.println("No. of compartments found " + compartmentNames.size());

        } catch (Throwable e) {
            System.err.println("ERROR searching for compartment");
        }

        return compartmentNames;
    }
}
