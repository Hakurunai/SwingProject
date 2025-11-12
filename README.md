# Find Boat Front

## Description

This is a desktop project realised to train me on Java


## Dependencies

This project uses Maven to manage dependencies. Maven will automatically download the required libraries when you build the project.

- Logger : log4j-core 2.25.2
- Config file : dotenv-java 3.2.0
- Mail : sib-api-v3-sdk 7.0.0
- Encryption : tink 1.19.0
- FTP : commons-net 3.12.0
- Unit test : junit-jupiter-api 6.0.0


## Requirement

- Java JDK 25
- Maven (for dependencies)
- [git](https://git-scm.com/install/) to clone the project
- A .env file was used for this project. In order for it to work, you need to provide your own version of it (I'll share
all my non sensitives keys, but you need to add some of yours for things like login/password). 
The file need to contain the following keys (do not share yours) :

```env
#Database file to load during init phase
DB_ORDER_FILE_PATH=datas/Commandes.txt
DB_PRODUCT_FILE_PATH=datas/Produits.txt

#Output
OUTPUT_FILE_PATH=output/
REVIEW_ORDER_FILE_NAME=orderReview.txt
OUTPUT_PRODUCT_FILE_NAME=productBackUp.backup
OUTPUT_ORDER_FILE_NAME=orderBackUp.backup

#Crypted output
TINK_CRYPTED_KEY=CJufidIPEmQKWAowdHlwZS5nb29nbGVhcGlzLmNvbS9nb29nbGUuY3J5cHRvLnRpbmsuQWVzR2NtS2V5EiIaICEVehmPrRlsH1Sg6NRICMfnb0bIchqV7zXRd/Je7tyHGAEQARibn4nSDyAB
OUTPUT_CRYPTED_FILE_PATH=output/crypted/
OUTPUT_CRYPTED_PRODUCT_FILE_NAME=product.enc
OUTPUT_CRYPTED_ORDER_FILE_NAME=order.enc

#Send blue data
SEND_BLUE_API_KEY=              ????????????????????
SEND_BLUE_MAIL_TARGET_MAIL=     ????????????????????
SEND_BLUE_MAIL_TARGET_NAME=     ????????????????????
SEND_BLUE_MAIL_SENDER_MAIL=     ????????????????????
SEND_BLUE_MAIL_SENDER_NAME=     ????????????????????

#FTP data
FTP_SERVER_URL=                 ????????????????????
FTP_SERVER_USERNAME=            ????????????????????
FTP_SERVER_PASSWORD=            ????????????????????
FTP_SERVER_PATH_TO_SEND=        ????????????????????
```


## Installation

1. Clone the project via git
2. Open it via your IDE (I used IntelliJ for this one) and resolve dependencies via Maven
3. Follow requirement concerning .env file
4. Launch the app via main class
