<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <head>
        <link rel="stylesheet" href="${url.resourcesPath}/css/login-styles.css" />
    </head>
    <div class="login-container">
        <div class="login-card">
            <div class="logo">
                <h1>Taskii</h1>
            </div>
            
            <div class="form-header">
                <h2>Sign in</h2>
            </div>
            
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="input-group">
                    <input type="text" 
                           id="username" 
                           name="username" 
                           value="${login.username!}" 
                           placeholder="Username*"
                           required 
                           autofocus />
                    <span class="input-icon user-icon">👤</span>
                </div>
                
                <div class="input-group">
                    <input type="password" 
                           id="password" 
                           name="password" 
                           placeholder="Password*"
                           required />
                    <span class="input-icon password-icon">🔒</span>
                </div>
                
                <div class="forgot-password">
                    <a href="${url.forgotPasswordUrl}">Forgot password?</a>
                </div>
                
                <button type="submit" id="kc-login" class="login-btn">
                    Login
                </button>
            </form>
            
            <div class="register-link">
                <span>Don't have an account? </span>
                <a href="${url.registrationUrl}">Register here!</a>
            </div>
        </div>
    </div>
</@layout.registrationLayout>