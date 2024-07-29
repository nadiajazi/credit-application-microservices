package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should send a payment notification"
    label 'notification_sent'
    input {
        triggeredBy('sendNotification()')
    }
    outputMessage {
        sentTo('payment-topic')
        body([
                amount            : $(regex('[0-9]+(\\.[0-9]+)?')),
                paymentMethod     : $(anyOf("PAYPAL", "CREDIT_CARD", "VISA", "MASTER_CARD", "BITCOIN")),
                customerFirstname : $(regex('[A-Za-z ]+')),
                customerLastname  : $(regex('[A-Za-z ]+')),
                customerEmail     : $(regex('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}'))
        ])
        headers {
            messagingContentType(applicationJson())
        }
    }
}
