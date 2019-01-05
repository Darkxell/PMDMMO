function Download-File {
    param(
        [String] $url,
        [String] $path
    )

    $client = New-Object System.Net.WebClient
    $client.DownloadFile($url, $path)
}

$java_info = Get-Command java -errorAction SilentlyContinue
if (!$java_info -or ($java_info.Version.Major -lt 8)) {
    exit 2 # "jre not installed"
}

# exit 3 # "could not connect to server"
# TODO: install (& verify?) jar
# exit 4 # "could not verify download"
# TODO: install data files
# exit 5 # "could not get game data"

Write-Host "Successfully installed!"

Start-Process 'D:\git\PMDMMO\out\artifacts\PMDMMOc_jar\PMDMMOc.jar'
    -NoNewWindow
