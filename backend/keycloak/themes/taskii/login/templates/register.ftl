<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <div class="kc-form-register">
        <form id="kc-register-form" action="${url.registrationAction}" method="post">
            <input type="text" name="firstName" placeholder="First Name" required />
            <input type="text" name="lastName" placeholder="Last Name" required />
            <input type="text" name="email" placeholder="Email" required />
            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <input type="submit" value="${msg("doRegister")}" />
        </form>
    </div>
</@layout.registrationLayout>
