Messages control images as files. What they do now is when you add a picutre it takes in a byte array of picture content:
```java
public void addPicture(byte[] newPictureContent)
```
this byte array is then converted directly to a picture file with a number determined by the next number available (stored in the picture.txt file) in a very similar way to how message ids are stored and created. 
```java
    try (BufferedReader bfr = new BufferedReader(new FileReader(pictureNumbers))) {
        pictureLocation = Integer.parseInt(bfr.readLine());
    } catch (Exception e) {
        System.out.println("Error reading picture number");
    }
    pictureFile = String.format("%d.jpg", pictureLocation);
    try (BufferedWriter bwr = new BufferedWriter(new FileWriter(pictureNumbers))) {
        bwr.write(Integer.toString(++pictureLocation));
    } catch (Exception e) {
        System.out.println("Error writing picture number");
    }
```
the file location to this picture is then stored in the message data 
```java
private String pictureFile;
```
You can then convert this image to a byte array and send it to the client that way, currently the network does not handle this very well but you can add this in very quickly. For help with this implementation see [imageNetwork.java](imageNetwork.java). 