import { sanitizePostContent } from "./sanitizePostContent";

describe("sanitizePostContent", () => {
  it.each([
    ["inline formatting", "<p><strong>a</strong><em>b</em><u>c</u><s>d</s><mark>e</mark><sub>f</sub><sup>g</sup></p>"],
    ["alignment", '<p style="text-align:justify">a</p>'],
    ["text color", '<p><span style="color:#fa5252">a</span></p>'],
    ["code blocks", '<pre class="language-css"><code class="language-css">a<br />b</code></pre>'],
    ["images", '<p><img src="https://example.com/a.png" alt="a" width="120" height="60" /></p>'],
    ["relative links", '<p><a href="/posts/x">a</a></p>'],
    [
      "tables",
      '<table style="border-collapse:collapse;width:50%" border="1"><caption>c</caption>' +
        '<colgroup><col style="width:30%" /><col style="width:70%" /></colgroup>' +
        '<thead><tr><th scope="col">a</th><th scope="col">b</th></tr></thead>' +
        '<tbody><tr><td style="background-color:#eee" colspan="2">c</td></tr></tbody></table>',
    ],
    ["column widths", '<table><colgroup><col style="width:20.0797%" /><col style="width:79.9203%" /></colgroup></table>'],
    [
      "cell, row and border colors",
      '<table><tbody><tr style="background-color:#ffeeaa;border-color:#0000ff">' +
        '<td style="background-color:#00ff00;border:3px dashed #ff0000">a</td>' +
        '<td style="border:1px solid rgb(1, 2, 3)">b</td></tr></tbody></table>',
    ],
  ])("keeps %s", (_name, html) => {
    expect(sanitizePostContent(html)).toBe(html);
  });

  it.each([
    ["scripts", "<p>a</p><script>alert(1)</script>", "<p>a</p>"],
    ["style elements", "<style>body{display:none}</style><p>a</p>", "<p>a</p>"],
    ["event handlers", '<img src="/a.png" onerror="alert(1)">', '<img src="/a.png" />'],
    ["javascript: links", '<a href="javascript:alert(1)">a</a>', "<a>a</a>"],
    ["obfuscated javascript: links", '<a href="java&#x0A;script:alert(1)">a</a>', "<a>a</a>"],
    ["iframes", '<iframe src="https://example.com"></iframe>', ""],
    ["svg payloads", '<svg onload="alert(1)"></svg>', ""],
    ["forms", '<form action="/x"><input name="a"></form>', ""],
    ["page-covering styles", '<p style="position:fixed;top:0;text-align:center">a</p>', '<p style="text-align:center">a</p>'],
    ["url() in styles", '<p style="background-color:url(https://example.com)">a</p>', "<p>a</p>"],
    ["url() in borders", '<p style="border:1px solid url(https://example.com)">a</p>', "<p>a</p>"],
    ["arbitrary classes", '<p class="mantine-Modal-root">a</p>', "<p>a</p>"],
    ["base64 images", '<img src="data:image/png;base64,AAAA" alt="a">', '<img alt="a" />'],
  ])("removes %s", (_name, html, expected) => {
    expect(sanitizePostContent(html)).toBe(expected);
  });

  it("protects links that open a new tab", () => {
    expect(sanitizePostContent('<a href="https://example.com" target="_blank">a</a>')).toBe(
      '<a href="https://example.com" target="_blank" rel="noopener noreferrer nofollow">a</a>'
    );
  });
});
