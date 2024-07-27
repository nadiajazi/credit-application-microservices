package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should update user montant"
    request {
        method PUT()
        urlPath("/api/v1/user/updateMontant") {
            headers {
                header('Authorization', $(regex('Bearer [a-zA-Z0-9-.]+')))
            }
            body(
                    userId: 1,
                    newMontant: 150.00
            )
            headers {
                contentType(applicationJson())
            }
        }
    }
    response {
        status OK()
    }
}
