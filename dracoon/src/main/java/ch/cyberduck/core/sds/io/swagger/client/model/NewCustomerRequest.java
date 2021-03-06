/*
 * DRACOON
 * REST Web Services for DRACOON<br>Version: 4.10.7-LTS  - built at: 2019-03-19 14:24:35<br><br><a title='Developer Information' href='https://developer.dracoon.com'>Developer Information</a>&emsp;&emsp;<a title='Get SDKs on GitHub' href='https://github.com/dracoon'>Get SDKs on GitHub</a>
 *
 * OpenAPI spec version: 4.10.7-LTS
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package ch.cyberduck.core.sds.io.swagger.client.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

/**
 * NewCustomerRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-13T14:25:40.305+02:00")
public class NewCustomerRequest {
  /**
   * Customer type
   */
  public enum CustomerContractTypeEnum {
    FREE("free"),
    
    DEMO("demo"),
    
    PAY("pay");

    private String value;

    CustomerContractTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CustomerContractTypeEnum fromValue(String text) {
      for (CustomerContractTypeEnum b : CustomerContractTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("customerContractType")
  private CustomerContractTypeEnum customerContractType = null;

  @JsonProperty("quotaMax")
  private Long quotaMax = null;

  @JsonProperty("userMax")
  private Integer userMax = null;

  @JsonProperty("firstAdminUser")
  private FirstAdminUser firstAdminUser = null;

  @JsonProperty("companyName")
  private String companyName = null;

  @JsonProperty("trialDays")
  private Integer trialDays = null;

  @JsonProperty("isLocked")
  private Boolean isLocked = null;

  @JsonProperty("customerAttributes")
  private CustomerAttributes customerAttributes = null;

    @JsonProperty("lockStatus")
    private Boolean lockStatus = null;

  @JsonProperty("activationCode")
  private String activationCode = null;

  @JsonProperty("providerCustomerId")
  private String providerCustomerId = null;

  public NewCustomerRequest customerContractType(CustomerContractTypeEnum customerContractType) {
    this.customerContractType = customerContractType;
    return this;
  }

   /**
   * Customer type
   * @return customerContractType
  **/
  @ApiModelProperty(example = "pay", required = true, value = "Customer type")
  public CustomerContractTypeEnum getCustomerContractType() {
    return customerContractType;
  }

  public void setCustomerContractType(CustomerContractTypeEnum customerContractType) {
    this.customerContractType = customerContractType;
  }

  public NewCustomerRequest quotaMax(Long quotaMax) {
    this.quotaMax = quotaMax;
    return this;
  }

   /**
    * Maximal disc space which can be allocated by customer in bytes.
   * @return quotaMax
  **/
   @ApiModelProperty(required = true, value = "Maximal disc space which can be allocated by customer in bytes.")
  public Long getQuotaMax() {
    return quotaMax;
  }

  public void setQuotaMax(Long quotaMax) {
    this.quotaMax = quotaMax;
  }

  public NewCustomerRequest userMax(Integer userMax) {
    this.userMax = userMax;
    return this;
  }

   /**
   * Maximal number of users
   * @return userMax
  **/
  @ApiModelProperty(required = true, value = "Maximal number of users")
  public Integer getUserMax() {
    return userMax;
  }

  public void setUserMax(Integer userMax) {
    this.userMax = userMax;
  }

  public NewCustomerRequest firstAdminUser(FirstAdminUser firstAdminUser) {
    this.firstAdminUser = firstAdminUser;
    return this;
  }

   /**
   * First admin user of a customer
   * @return firstAdminUser
  **/
  @ApiModelProperty(required = true, value = "First admin user of a customer")
  public FirstAdminUser getFirstAdminUser() {
    return firstAdminUser;
  }

  public void setFirstAdminUser(FirstAdminUser firstAdminUser) {
    this.firstAdminUser = firstAdminUser;
  }

  public NewCustomerRequest companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

   /**
   * Company name
   * @return companyName
  **/
  @ApiModelProperty(value = "Company name")
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public NewCustomerRequest trialDays(Integer trialDays) {
    this.trialDays = trialDays;
    return this;
  }

   /**
   * Number of days left for trial period (relevant only for type &#x60;demo&#x60;)
   * @return trialDays
  **/
  @ApiModelProperty(value = "Number of days left for trial period (relevant only for type `demo`)")
  public Integer getTrialDays() {
    return trialDays;
  }

  public void setTrialDays(Integer trialDays) {
    this.trialDays = trialDays;
  }

  public NewCustomerRequest isLocked(Boolean isLocked) {
    this.isLocked = isLocked;
    return this;
  }

   /**
    * Customer is locked: * &#x60;false&#x60; - unlocked * &#x60;true&#x60; - locked  All users of this customer will be blocked and can not login anymore. (default: &#x60;false&#x60;)
   * @return isLocked
  **/
   @ApiModelProperty(example = "false", value = "Customer is locked: * `false` - unlocked * `true` - locked  All users of this customer will be blocked and can not login anymore. (default: `false`)")
   public Boolean isIsLocked() {
    return isLocked;
  }

  public void setIsLocked(Boolean isLocked) {
    this.isLocked = isLocked;
  }

  public NewCustomerRequest customerAttributes(CustomerAttributes customerAttributes) {
    this.customerAttributes = customerAttributes;
    return this;
  }

   /**
   * Customer attributes
   * @return customerAttributes
  **/
  @ApiModelProperty(value = "Customer attributes")
  public CustomerAttributes getCustomerAttributes() {
    return customerAttributes;
  }

  public void setCustomerAttributes(CustomerAttributes customerAttributes) {
    this.customerAttributes = customerAttributes;
  }

    public NewCustomerRequest lockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
        return this;
    }

    /**
     * &#x60;DEPRECATED&#x60;: Customer lock status: * &#x60;false&#x60; - unlocked * &#x60;true&#x60; - locked  Please use &#x60;isLocked&#x60; instead. All users of this customer will be blocked and can not login anymore. (default: &#x60;false&#x60;)
     *
     * @return lockStatus
     **/
    @ApiModelProperty(example = "false", value = "`DEPRECATED`: Customer lock status: * `false` - unlocked * `true` - locked  Please use `isLocked` instead. All users of this customer will be blocked and can not login anymore. (default: `false`)")
    public Boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

  public NewCustomerRequest activationCode(String activationCode) {
    this.activationCode = activationCode;
    return this;
  }

   /**
   * &#x60;DEPRECATED&#x60;: Customer activation code string: * valid only for types &#x60;free&#x60; and &#x60;demo&#x60; * for &#x60;pay&#x60; customers it is empty
   * @return activationCode
  **/
  @ApiModelProperty(value = "`DEPRECATED`: Customer activation code string: * valid only for types `free` and `demo` * for `pay` customers it is empty")
  public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public NewCustomerRequest providerCustomerId(String providerCustomerId) {
    this.providerCustomerId = providerCustomerId;
    return this;
  }

    /**
     * &#x60;DEPRECATED&#x60;: Provider customer ID
   * @return providerCustomerId
     **/
    @ApiModelProperty(value = "`DEPRECATED`: Provider customer ID")
  public String getProviderCustomerId() {
    return providerCustomerId;
  }

  public void setProviderCustomerId(String providerCustomerId) {
    this.providerCustomerId = providerCustomerId;
  }


    @Override
    public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewCustomerRequest newCustomerRequest = (NewCustomerRequest) o;
    return Objects.equals(this.customerContractType, newCustomerRequest.customerContractType) &&
        Objects.equals(this.quotaMax, newCustomerRequest.quotaMax) &&
        Objects.equals(this.userMax, newCustomerRequest.userMax) &&
        Objects.equals(this.firstAdminUser, newCustomerRequest.firstAdminUser) &&
        Objects.equals(this.companyName, newCustomerRequest.companyName) &&
        Objects.equals(this.trialDays, newCustomerRequest.trialDays) &&
        Objects.equals(this.isLocked, newCustomerRequest.isLocked) &&
        Objects.equals(this.customerAttributes, newCustomerRequest.customerAttributes) &&
        Objects.equals(this.lockStatus, newCustomerRequest.lockStatus) &&
        Objects.equals(this.activationCode, newCustomerRequest.activationCode) &&
        Objects.equals(this.providerCustomerId, newCustomerRequest.providerCustomerId);
  }

  @Override
  public int hashCode() {
      return Objects.hash(customerContractType, quotaMax, userMax, firstAdminUser, companyName, trialDays, isLocked, customerAttributes, lockStatus, activationCode, providerCustomerId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewCustomerRequest {\n");
    
    sb.append("    customerContractType: ").append(toIndentedString(customerContractType)).append("\n");
    sb.append("    quotaMax: ").append(toIndentedString(quotaMax)).append("\n");
    sb.append("    userMax: ").append(toIndentedString(userMax)).append("\n");
    sb.append("    firstAdminUser: ").append(toIndentedString(firstAdminUser)).append("\n");
    sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
    sb.append("    trialDays: ").append(toIndentedString(trialDays)).append("\n");
    sb.append("    isLocked: ").append(toIndentedString(isLocked)).append("\n");
    sb.append("    customerAttributes: ").append(toIndentedString(customerAttributes)).append("\n");
      sb.append("    lockStatus: ").append(toIndentedString(lockStatus)).append("\n");
    sb.append("    activationCode: ").append(toIndentedString(activationCode)).append("\n");
    sb.append("    providerCustomerId: ").append(toIndentedString(providerCustomerId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

