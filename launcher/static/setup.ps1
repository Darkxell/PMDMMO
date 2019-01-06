function Download-Jar {
    # TODO: set up dynamic installation and patches
    $url = "https://s3-us-west-1.amazonaws.com/xsduan/alpha0.1.0h-10.zip"
    $zip = [guid]::NewGuid().Guid + ".zip"
    $client = New-Object System.Net.WebClient
    $client.DownloadFile($url, $zip)
    Expand-Archive -Path $zip -DestinationPath "."
    Remove-Item $zip
}

$java_info = Get-Command java -errorAction SilentlyContinue
if (!$java_info) {
    exit 2 # jre not installed
}

$min = New-Object -TypeName System.Version -ArgumentList "8.0.1810.0"
if ($java_info.Version -lt $min) {
    exit 3 # jre must be >=1.8
}

# TODO: standardize archive format
$path = "test.jar"
if (!(Test-Path $path)) {
    # TODO: verification
    Write-Host "Could not find executable, installing."
    Download-Jar
}

Start-Process $path
