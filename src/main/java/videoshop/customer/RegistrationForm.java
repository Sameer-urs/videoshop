
/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.validation.Errors;

/**
 * This is a so called form-backing class to prepare the form display (in
 * {@link CustomerController#register(org.springframework.ui.Model, RegistrationForm)})
 * and during form submission (in
 * {@link CustomerController#registerNew(RegistrationForm, Errors)}).
 * <p>
 * The latter will have the form data bound to constructor of this class and the
 * {@code @Valid} annotation on the controller method parameter will cause the
 * validation annotation on the fields being triggered. For invalid values,
 * the {@link Errors} instance gets a field error registered and a couple of
 * internationalization codes registered that Spring will try to resolve
 * against the messages declared in {@code messages.properties}.
 * <p>
 * The codes will look as follows:
 * <ul>
 * <li>{@code $SimpleAnnotationName.$SimpleBeanTypeName.$propertyName} - e.g.
 * {@code NotEmpty.registrationForm.name}</li>
 * <li>{@code $SimpleAnnotationName.$propertyName} - e.g. {@code NotEmpty.name}</li>
 * <li>{@code $SimpleAnnotationName.$fullyQualifiedTypeName} - e.g.
 * {@code NotEmpty.java.lang.String}</li>
 * <li>{@code $SimpleAnnotationName} - e.g. {@code NotEmpty}</li>
 * </ul>
 * This allows you to declare generic error messages based on the problem
 * rejected, but also very specific ones for the particular binding type
 * and field.
 * <p>
 * Advanced validations can be implemented by declaring custom validation
 * methods on the form-backing class, evaluating the state and manually
 * rejecting field values (see {@link #validate(Errors)}).
 *
 * @author Paul Henke
 * @author Oliver Drotbohm
 */
class RegistrationForm {

    @NotEmpty
    private final String name;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).*$",
        message = "Password must contain at least one uppercase letter and one number"
    )
    private final String password;

    @NotEmpty(message = "Please confirm your password")
    private final String confirmPassword;

    @NotEmpty
    private final String address;

    /**
     * Constructor used to bind the registration form data.
     *
     * @param name the username
     * @param password the chosen password
     * @param confirmPassword the password entered a second time
     * @param address the user's address
     */
    public RegistrationForm(
            String name,
            String password,
            String confirmPassword,
            String address) {

        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Performs additional validation that cannot be expressed using
     * the field annotations alone.
     *
     * Checks that the password and confirmation match.
     *
     * This method must be called by the controller or a registered
     * validator; Spring does not invoke it automatically just because
     * it is named validate.
     *
     * @param errors the object used to register validation errors
     */
    public void validate(Errors errors) {

        if (password != null
                && confirmPassword != null
                && !password.equals(confirmPassword)) {

            errors.rejectValue(
                "confirmPassword",
                "registration.password.mismatch",
                "Passwords do not match"
            );
        }
    }
}
