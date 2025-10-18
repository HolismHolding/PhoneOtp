<#macro layoutBody>
<!DOCTYPE html>
<#if locale?? && locale.currentLanguageTag??>
<html lang="${locale.currentLanguageTag}">
<#else>
<html lang="en">
</#if>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${msg("loginTitle", realm.displayName)!realm.displayName}</title>

    <link rel="icon" href="${url.resourcesPath}/img/temp.png" />
    <link rel="stylesheet" type="text/css" href="${url.resourcesPath}/css/styles.css" />
</head>

<body class="${properties.kcBodyClass!}">
    <div class="kc-page-wrapper">
        <header class="kc-header">
            <h1 class="kc-header-title">${realm.displayName!''}</h1>
        </header>

        <main class="kc-content">
            <#nested/>
        </main>

        <footer class="kc-footer">
            <p>&copy; ${.now?string("yyyy")} ${realm.displayName!''}</p>
        </footer>
    </div>
</body>
</html>
</#macro>
