package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return user montant by token"
    request {
        method GET()
        urlPath("/api/v1/user/montant") {
            headers {
                header('Authorization', $(regex('Bearer [a-zA-Z0-9-.]+')))
            }
        }
    }
    response {
        status OK()
        body(
                100.00
        )
        headers {
            contentType(applicationJson())
        }
    }
}
