# Core SDK

The levelup-core-lib provides client-server communication with the LevelUp web
service.  This library is divided into four different broad components:
networking, request factories, JSON factories, and model objects.  The
networking component handles the actual client-server communication.  The
request factories generate requests to send to the LevelUp web service (via the
networking component).  The JSON factories parse the responses from the LevelUp
web service into Java model objects.

## Network Component

The networking component wraps the full power of the standard Android HTTP
libraries with a set of classes that offer a significantly simplified
interface.  As is a common theme in the LevelUp SDK design, the networking
component is built in layers.  The hierarchy of these layers can be seen in the
following diagram:  ![LevelUp Networking Class
Hierarchy](../docs/network_layer.png "LevelUp Networking Class Hierarchy")

### HTTP Layer

The first layer is the generic HTTP layer, which provides generic Request and
Response objects that encapsulate generic HTTP communication with both the
streaming and buffered implementations.  There are two ways to think about
networking: streaming and buffered.  Because data is not generally delivered
from the network all at once, the low-level Android HTTP library operates on a
streaming interface.  A buffered interface could be built on top of a streaming
interface, waiting to deliver data to a client until all of the data has been
streamed over the network.

A streaming interface has performance and memory benefits.  For example, if a
client is downloading a large image from a server, it might write the stream to
disk as it comes in.  The entire image won't be kept in RAM and it is already
mostly on disk once the network connection finishes.  On the other hand, a
stream can be interrupted at any time and a client would need to be written to
handle various errors that could occur throughout the streaming process.

A buffered interface has limitations on the total size of data that can be
transmitted or received, as the entire network message must fit in memory.  On
the other hand, a buffered interface is much simpler from an error handling
standpoint since error handling occurs up front when the buffering occurs.

### LevelUp web service API Layer

The second layer is a set of LevelUp web service API layer, which abstract away
authentication and other aspects of communication with the LevelUp web service.
LevelUp client apps and the LevelUp web service communicate over a RESTful API
composed of small JSON messages.  Because these messages are small, they don't
require a streaming interface.  Therefore, the web service API layer is fully
buffered.

## RequestFactory Component

Each of the LevelUp web service endpoints offers a different API.  To simplify
interaction with these endpoints, levelup-core-lib provides factory classes to
build Request objects to send to the LevelUp web service.

## JsonFactory Component

A Request created by a RequestFactory is sent via the Network component,
resulting in an ``LevelUpResponse`` object.  Requests are associated
with a corresponding `JsonFactory` object that parses the response into a model
object.

## Model Component

The responses from the LevelUp web service can be parsed via a JsonFactory into
a Java model object.  By convention, these Model objects are immutable and
Parcelable.

### Use of Lombok

To make it easier to maintain our Java model objects, we use Project Lombok.
This tool automatically generates constructors, getters, builders, etc. based
on annotations.

Instead of editing the generated models in `src/com/scvngr/levelup/core/model/`
directly, please edit the Lombok'd versions, which are located in
`src-lombok/com/scvngr/levelup/core/model/`.

Then, from this project, run:

    ant delombok

This will generate the models' classes, including their getters, constructors,
and builders. As we don't want to mandate the use of Lombok in all SDK users'
IDEs, the delombok'd files (generated with the aforementioned ant task and
commingled with the rest of the source) are the ones that should be used
directly.

# Using the SDK

##Overview

A typical flow for using levelup-core-lib is as follows:

1. Construct a `RequestFactory`.
2. Call one of the factory methods on the `RequestFactory` to get an
   `AbstractRequest` object.
3. Call `ApiConnection.send()` to transmit the `AbstractRequest` object receive
   an `LevelUpResponse` object.
4. Construct the appropriate `JsonFactory` object to parse the
   `LevelUpResponse` object into a Model object.

Specific example flows are documented in more detail below:

##Registering

### Via LevelUp

1. Call `UserRequestFactory.buildRegisterRequest()`, to obtain an
   `AbstractRequest` to register a new user.
2. Send the Request.
3. Parse the resulting `LevelUpResponse` object with the
   `UserJsonFactory`.
4. Use the same credentials used to register the `User` to then Log them in
   using the steps below.

##Logging In

### Via LevelUp

1. Call `AccessTokenRequestFactory.buildLoginRequest()`, to obtain an
   `AbstractRequest` to login an existing user.
2. Send the `AbstractRequest`.
3. Parse the resulting `LevelUpResponse` object with
   `AccessTokenJsonFactory`, which results in a `AccessToken` model object.
4. Persist the `AccessToken` model object to authenticate other requests to the
   LevelUp web service.

##Authenticating further requests to the LevelUp web service

NOTE: `AccessToken`s should be considered as sensitive user data and should be
protected accordingly.

1. Once you have obtained your `AccessToken` by logging in as described above,
   you should persist it however you typically persist data in your app.
2. You will need to write a class that implements `AccessTokenRetriever` (and
   `Parcelable`) which will retrieve the cached `AccessToken`.
3. For every `RequestFactory` that you wish to use the cached `AccessToken` in,
   pass an instance of your class that implements `AccessTokenRetriever` to the
   constructor.

##Obtaining a Payment Token

1. After logging in or registering, the user ID and access token are available
   to authenticate future requests to the LevelUp web service.
2. Call `PaymentTokenRequestFactory.buildGetPaymentTokenRequest()` to obtain an
   `AbstractRequest` to get information for the current user.
3. Send the `AbstractRequest`.
4. If the request returns `LevelUpStatus.ERROR_NOT_FOUND`, then the user is not
   payment eligible and a new credit/debit card must be added to their account
   with the steps below.
5. If the request returns `LevelUpStatus.OK`, then parse the resulting
   `LevelUpResponse` object with `PaymentTokenJsonFactory`, which
results in a `PaymentToken` model object.
6. The QR code used for payment can be generated using the
   `PaymentToken.getData()`. For more information on how to generate the proper QR
   code, see [Generating QR Code][qr_code]

##Adding a Credit Card

1. After logging in or registering, the user ID and access token are available
   to authenticate future requests to the LevelUp web service.
2. Call `CreditCardRequestFactory.buildCreateCardRequest()` to obtain a
   `AbstractRequest` to add a new credit card to the user's account.
3. Send the `AbstractRequest`.
4. Parse the resulting `LevelUpResponse` object with
   `CreditCardJsonFactory`, which results in a `CreditCard` model object.

##Parsing Errors from responses

If the web service receives a request that it cannot process, it will typically
respond with a JSON array describing the errors that occurred. To parse these
errors:

1. Use `ErrorJsonFactory.fromList(new JSONArray(response.getData()))`.
2. If a `JSONException` is thrown, then there were no well formed errors from
   the server and you should use fallback generic error messages.

[qr_code]: #qr_code

<h2 id="qr_code">Generating QR Code</h2>

We Use Zebra Crossing (zxing v2.1) to generate the QR Codes in our app. While
we use a slightly more complicated process to generate our QR codes, here is
some sample code to generate a bitmap to display to the user.

    // Default the code to no tip and no color.
    final String qrCodeDataString = LevelUpCode.encodeLevelUpCode(paymentToken.getData(), 0, 0));
    final MultiFormatWriter writer = new MultiFormatWriter();
    final BitMatrix result =
          writer.encode(qrCodeDataString, BarcodeFormat.QR_CODE,
                        CODE_HEIGHT, CODE_WIDTH, null);
    final int width = result.getWidth();
    final int height = result.getHeight();
    final int[] pixels = new int[width * height];
    // All are 0, or black, by default
    for (int y = 0; y < height; y++) {
        final int offset = y * width;
        for (int x = 0; x < width; x++) {
            if (result.get(x, y)) {
                pixels[offset + x] = Color.BLACK;
            } else {
                pixels[offset + x] = Color.WHITE;
            }
        }
    }
    
    return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565);

