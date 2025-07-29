<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <form action="${url.loginAction}" method="post">
        <label for="username">${msg("usernameOrEmail")}</label>
        <input type="text" id="username" name="username" />
        <input type="submit" value="${msg("doSubmit")}" />
    </form>
</@layout.registrationLayout>
