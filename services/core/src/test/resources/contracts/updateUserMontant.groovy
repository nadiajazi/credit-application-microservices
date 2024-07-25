package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should update user montant"
    request {
        method PUT()
        urlPath("/user/updateMontant") {
            headers {
                header('Authorization', $(regex('Bearer [a-zA-Z0-9-.]+')))
            }
            body(
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
