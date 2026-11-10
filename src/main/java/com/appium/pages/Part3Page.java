 
package com.appium.pages;
 
import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.xml.parsers.DocumentBuilderFactory;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
 
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
 
public class Part3Page {
 
    private static final Logger log =
            LoggerFactory.getLogger(Part3Page.class);
 
    private final AndroidDriver driver;
    private final WebDriverWait wait;
 
    private final By part3Button =
            AppiumBy.accessibilityId("Part 3");
 
    private final By productList =
            AppiumBy.accessibilityId("Product List");
 
    private final By total =
            AppiumBy.accessibilityId("Total");
 
    private final Map<String, ProductData> collectedProducts =
            new LinkedHashMap<>();
 
    private final Set<String> collectedImageNames =
            new LinkedHashSet<>();
 
    private final Pattern amountPattern =
            Pattern.compile("[0-9]+(?:\\.[0-9]+)?");
 
    public Part3Page(AndroidDriver driver) {
 
        this.driver = driver;
 
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }
 
    // --------------------------------------------------
    // OPEN PART 3
    // --------------------------------------------------
 
    public void openPart3() {
 
        wait.until(
                ExpectedConditions.elementToBeClickable(part3Button)
        ).click();
 
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(productList)
        );
 
        log.info("Part 3 opened successfully.");
    }
 
    // --------------------------------------------------
    // TAT-68: DISPLAYED OVERALL TOTAL
    // --------------------------------------------------
 
    public double getDisplayedTotal() {
 
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(total)
        );
 
        String value = readElementValue(element);
 
        log.info("Displayed Total: {}", value);
 
        return extractAmount(value);
    }
 
    // --------------------------------------------------
    // COLLECT ALL PRODUCTS WHILE SCROLLING
    // EXISTING WORKING LOGIC - UNCHANGED
    // --------------------------------------------------
 
    public List<ProductData> getAllProducts() {
 
        collectedProducts.clear();
        collectedImageNames.clear();
 
        collectVisibleProductCards();
 
        int unchangedCount = 0;
 
        for (int scroll = 1; scroll <= 60; scroll++) {
 
            String previousSignature =
                    getVisibleCardSignature();
 
            slowScrollUp();
 
            collectVisibleProductCards();
 
            String currentSignature =
                    getVisibleCardSignature();
 
            if (currentSignature.equals(previousSignature)) {
 
                unchangedCount++;
 
            } else {
 
                unchangedCount = 0;
            }
 
            log.info(
                    "Scroll {} | Products: {} | Images: {}",
                    scroll,
                    collectedProducts.size(),
                    collectedImageNames.size()
            );
 
            if (unchangedCount >= 4) {
 
                log.info("Reached bottom of product list.");
 
                break;
            }
        }
 
        // Read final visible products once more.
        collectVisibleProductCards();
 
        log.info(
                "Total unique products collected: {}",
                collectedProducts.size()
        );
 
        for (ProductData product : collectedProducts.values()) {
 
            log.info("{}", product);
        }
 
        double calculatedTotal =
                collectedProducts.values()
                        .stream()
                        .mapToDouble(ProductData::getPrice)
                        .sum();
 
        log.info(
                "Calculated product total: {}",
                calculatedTotal
        );
 
        return new ArrayList<>(collectedProducts.values());
    }
 
    // --------------------------------------------------
    // READ PRODUCT CARDS FROM FRESH PAGE SOURCE
    // EXISTING WORKING LOGIC - UNCHANGED
    // --------------------------------------------------
 
    private void collectVisibleProductCards() {
 
        try {
 
            Document document = parsePageSource();
 
            NodeList allNodes =
                    document.getElementsByTagName("*");
 
            for (int i = 0; i < allNodes.getLength(); i++) {
 
                Node brandNode = allNodes.item(i);
 
                if (!"fakeBrand".equals(
                        getAttribute(brandNode, "content-desc"))) {
 
                    continue;
                }
 
                Node card = findProductCard(brandNode);
 
                if (card == null) {
                    continue;
                }
 
                Node priceNode =
                        findDescendant(card, "fakePrice");
 
                if (priceNode == null) {
                    continue;
                }
 
                String brand = getNodeValue(brandNode);
 
                String priceText = getNodeValue(priceNode);
 
                String description =
                        getDescendantValue(card, "fakeDescription");
 
                String imageName =
                        getDescendantValue(card, "fakeImage");
 
                brand = brand.replaceFirst(
                        "(?i)^Brand:\\s*",
                        ""
                ).trim();
 
                if (brand.isEmpty() || priceText.isEmpty()) {
                    continue;
                }
 
                double price = extractAmount(priceText);
 
                // Keep image collection independent of deduplication.
                if (!imageName.isEmpty()) {
 
                    collectedImageNames.add(imageName);
                }
 
                // Image name deliberately excluded from key.
                String productKey =
                        brand + "|" + price + "|" + description;
 
                ProductData product = new ProductData(
                        brand,
                        price,
                        imageName,
                        description
                );
 
                collectedProducts.putIfAbsent(
                        productKey,
                        product
                );
            }
 
        } catch (Exception e) {
 
            log.error("Unable to collect product cards", e);
        }
    }
 
    // Find nearest ancestor containing one brand and one price.
    private Node findProductCard(Node brandNode) {
 
        Node parent = brandNode.getParentNode();
 
        while (parent != null
                && parent.getNodeType() == Node.ELEMENT_NODE) {
 
            int brandCount =
                    countDescendants(parent, "fakeBrand");
 
            int priceCount =
                    countDescendants(parent, "fakePrice");
 
            if (brandCount == 1 && priceCount == 1) {
 
                return parent;
            }
 
            parent = parent.getParentNode();
        }
 
        return null;
    }
 
    private int countDescendants(
            Node parent,
            String accessibilityId) {
 
        int count = 0;
 
        NodeList children = parent.getChildNodes();
 
        for (int i = 0; i < children.getLength(); i++) {
 
            Node child = children.item(i);
 
            if (accessibilityId.equals(
                    getAttribute(child, "content-desc"))) {
 
                count++;
            }
 
            count += countDescendants(
                    child,
                    accessibilityId
            );
        }
 
        return count;
    }
 
    // --------------------------------------------------
    // VISIBLE PRODUCT SIGNATURE
    // EXISTING WORKING LOGIC - UNCHANGED
    // --------------------------------------------------
 
    private String getVisibleCardSignature() {
 
        StringBuilder signature = new StringBuilder();
 
        try {
 
            Document document = parsePageSource();
 
            NodeList allNodes =
                    document.getElementsByTagName("*");
 
            for (int i = 0; i < allNodes.getLength(); i++) {
 
                Node brandNode = allNodes.item(i);
 
                if (!"fakeBrand".equals(
                        getAttribute(brandNode, "content-desc"))) {
 
                    continue;
                }
 
                Node card = findProductCard(brandNode);
 
                if (card == null) {
                    continue;
                }
 
                Node priceNode =
                        findDescendant(card, "fakePrice");
 
                if (priceNode == null) {
                    continue;
                }
 
                signature
                        .append(getNodeValue(brandNode))
                        .append("|")
                        .append(getNodeValue(priceNode))
                        .append("|")
                        .append(getDescendantValue(
                                card,
                                "fakeDescription"
                        ))
                        .append("|")
                        .append(getDescendantValue(
                                card,
                                "fakeImage"
                        ))
                        .append(";");
 
            }
 
        } catch (Exception e) {
 
            log.warn(
                    "Unable to create visible product signature: {}",
                    e.getMessage()
            );
        }
 
        return signature.toString();
    }
 
    // --------------------------------------------------
    // EXISTING WORKING SCROLL GESTURE - UNCHANGED
    // --------------------------------------------------
 
    private void slowScrollUp() {
 
        PointerInput finger = new PointerInput(
                PointerInput.Kind.TOUCH,
                "finger"
        );
 
        Sequence swipe = new Sequence(finger, 1);
 
        swipe.addAction(
                finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        600,
                        1900
                )
        );
 
        swipe.addAction(
                finger.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );
 
        swipe.addAction(
                finger.createPointerMove(
                        Duration.ofMillis(1200),
                        PointerInput.Origin.viewport(),
                        600,
                        1100
                )
        );
 
        swipe.addAction(
                finger.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );
 
        driver.perform(Arrays.asList(swipe));
    }
 
    // --------------------------------------------------
    // TAT-69: DISPLAYED BRAND TOTALS
    // --------------------------------------------------
 
    public Map<String, Double> getDisplayedBrandTotals() {
 
        Map<String, Double> totals =
                new LinkedHashMap<>();
 
        try {
 
            // Read the current screen before product scrolling.
            Document document = parsePageSource();
 
            NodeList allNodes =
                    document.getElementsByTagName("*");
 
            Pattern totalPattern = Pattern.compile(
                    "(?i)Total\\s*\\(?\\s*"
                    + "([\\p{L}][\\p{L}\\s.-]*?)"
                    + "\\s*\\)?\\s*:?\\s*\\$?\\s*"
                    + "([0-9]+(?:\\.[0-9]+)?)"
            );
 
            for (int i = 0; i < allNodes.getLength(); i++) {
 
                Node node = allNodes.item(i);
 
                String text =
                        getAttribute(node, "text");
 
                String desc =
                        getAttribute(node, "content-desc");
 
                if ((text == null || text.trim().isEmpty())
                        && (desc == null || desc.trim().isEmpty())) {
 
                    continue;
                }
 
                log.info(
                        "TOTAL ELEMENT | text={} | content-desc={}",
                        text,
                        desc
                );
 
                // Check both attributes independently.
                String[] values = {text, desc};
 
                for (String value : values) {
 
                    if (value == null || value.trim().isEmpty()) {
                        continue;
                    }
 
                    Matcher matcher =
                            totalPattern.matcher(value.trim());
 
                    if (matcher.find()) {
 
                        String brand =
                                matcher.group(1).trim();
 
                        double amount =
                                Double.parseDouble(matcher.group(2));
 
                        // Exclude the overall Total element.
                        if (!brand.isEmpty()
                                && !brand.equalsIgnoreCase("Total")
                                && !brand.equalsIgnoreCase("Selected Total")) {
 
                            totals.put(brand, amount);
 
                            log.info(
                                    "Brand total extracted: {} = {}",
                                    brand,
                                    amount
                            );
                        }
                    }
                }
            }
 
        } catch (Exception e) {
 
            log.error(
                    "Unable to read displayed brand totals",
                    e
            );
        }
 
        log.info(
                "Displayed brand totals: {}",
                totals
        );
 
        return totals;
    }
 
    // --------------------------------------------------
    // TAT-70: IMAGE NAMES
    // EXISTING WORKING LOGIC - UNCHANGED
    // --------------------------------------------------
 
    public Set<String> getCollectedImageNames() {
 
        return new LinkedHashSet<>(collectedImageNames);
    }
 
    // --------------------------------------------------
    // XML PAGE SOURCE HELPERS
    // --------------------------------------------------
 
    private Document parsePageSource() throws Exception {
 
        String source = driver.getPageSource();
 
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
 
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
        );
 
        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false
        );
 
        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false
        );
 
        return factory.newDocumentBuilder().parse(
                new InputSource(new StringReader(source))
        );
    }
 
    private String getAttribute(
            Node node,
            String attribute) {
 
        if (node instanceof Element) {
 
            return ((Element) node).getAttribute(attribute);
        }
 
        return "";
    }
 
    private Node findDescendant(
            Node parent,
            String accessibilityId) {
 
        NodeList children = parent.getChildNodes();
 
        for (int i = 0; i < children.getLength(); i++) {
 
            Node child = children.item(i);
 
            if (accessibilityId.equals(
                    getAttribute(child, "content-desc"))) {
 
                return child;
            }
 
            Node result = findDescendant(
                    child,
                    accessibilityId
            );
 
            if (result != null) {
                return result;
            }
        }
 
        return null;
    }
 
    private String getDescendantValue(
            Node parent,
            String accessibilityId) {
 
        Node element = findDescendant(
                parent,
                accessibilityId
        );
 
        return element == null
                ? ""
                : getNodeValue(element);
    }
 
    private String getNodeValue(Node node) {
 
        String text = getAttribute(node, "text");
 
        if (text == null || text.trim().isEmpty()) {
 
            text = getAttribute(node, "content-desc");
        }
 
        return text == null
                ? ""
                : text.trim();
    }
 
    // --------------------------------------------------
    // SELENIUM ELEMENT VALUE HELPER
    // --------------------------------------------------
 
    private String readElementValue(WebElement element) {
 
        String value = element.getText();
 
        if (value == null || value.trim().isEmpty()) {
 
            value = element.getAttribute("contentDescription");
        }
 
        if (value == null || value.trim().isEmpty()) {
 
            value = element.getAttribute("content-desc");
        }
 
        if (value == null || value.trim().isEmpty()) {
 
            value = element.getAttribute("text");
        }
 
        return value == null
                ? ""
                : value.trim();
    }
 
    // --------------------------------------------------
    // AMOUNT EXTRACTION
    // --------------------------------------------------
 
    private double extractAmount(String value) {
 
        Matcher matcher = amountPattern.matcher(value);
 
        if (!matcher.find()) {
 
            throw new IllegalArgumentException(
                    "Could not extract amount from: " + value
            );
        }
 
        return Double.parseDouble(matcher.group());
    }
 
    // --------------------------------------------------
    // PRODUCT DATA
    // --------------------------------------------------
 
    public static class ProductData {
 
        private final String brand;
        private final double price;
        private final String imageName;
        private final String description;
 
        public ProductData(
                String brand,
                double price,
                String imageName,
                String description) {
 
            this.brand = brand;
            this.price = price;
            this.imageName = imageName;
            this.description = description;
        }
 
        public String getBrand() {
            return brand;
        }
 
        public double getPrice() {
            return price;
        }
 
        public String getImageName() {
            return imageName;
        }
 
        public String getDescription() {
            return description;
        }
 
        @Override
        public String toString() {
 
            return "Brand: " + brand
                    + " | Price: " + price
                    + " | Image: " + imageName;
        }
    }
}

 