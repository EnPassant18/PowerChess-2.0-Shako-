<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="<#if pathToRoot??>${pathToRoot}/</#if>css/normalize.css">
    <link rel="stylesheet" href="<#if pathToRoot??>${pathToRoot}/</#if>css/html5bp.css">
    <link rel="stylesheet" href="<#if pathToRoot??>${pathToRoot}/</#if>css/main.css">
  </head>
  <body>
     <#if content??>
         ${content}
     </#if>
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="<#if pathToRoot??>${pathToRoot}/</#if>js/jquery-3.1.1.js"></script>
     <#if scripts??>
         ${scripts}
     </#if>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
