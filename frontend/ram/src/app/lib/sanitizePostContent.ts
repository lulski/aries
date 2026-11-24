import sanitizeHtml from "sanitize-html";

const COLOR = [
  /^#[0-9a-f]{3,8}$/i,
  /^(rgb|hsl)a?\([\d\s.,%/]+\)$/i,
  /^[a-z]+$/i,
];
const SIZE = [/^(auto|0|\d{0,5}\.?\d{1,6}(px|pt|em|rem|%))$/i];
const SIZES = [/^((auto|0|\d{0,5}\.?\d{1,6}(px|pt|em|rem|%))\s*){1,4}$/i];
const KEYWORD = [/^[a-z-]+$/i];
// TinyMCE merges a border's width, style and color into this shorthand.
const BORDER = [
  /^\d{0,5}\.?\d{1,6}(px|pt|em|rem) [a-z]+ (#[0-9a-f]{3,8}|[a-z]+|(rgb|hsl)a?\([\d\s.,%/]+\))$/i,
];

/**
 * Reduces post HTML to the markup the TinyMCE editor produces (plus what posts
 * written with the previous editor contain). Posts are rendered as raw HTML, so
 * this is what keeps scripts out of the page: don't add tags, attributes or
 * styles lightly.
 */
export function sanitizePostContent(html: string): string {
  return sanitizeHtml(html, {
    allowedTags: [...sanitizeHtml.defaults.allowedTags, "del", "img", "ins"],
    selfClosing: [...sanitizeHtml.defaults.selfClosing, "col"],
    allowedAttributes: {
      "*": ["style"],
      a: ["href", "rel", "target", "title"],
      col: ["span"],
      img: ["alt", "height", "src", "title", "width"],
      ol: ["start", "type"],
      table: ["border", "cellpadding", "cellspacing"],
      td: ["colspan", "rowspan"],
      th: ["colspan", "rowspan", "scope"],
    },
    allowedClasses: {
      code: ["language-*"],
      pre: ["language-*"],
    },
    allowedStyles: {
      "*": {
        color: COLOR,
        "background-color": COLOR,
        border: BORDER,
        "border-collapse": KEYWORD,
        "border-color": COLOR,
        "border-style": KEYWORD,
        "border-width": SIZES,
        float: KEYWORD,
        height: SIZE,
        "list-style-type": KEYWORD,
        "margin-left": SIZE,
        "margin-right": SIZE,
        padding: SIZES,
        "padding-left": SIZE,
        "padding-right": SIZE,
        "text-align": KEYWORD,
        "text-decoration": KEYWORD,
        "vertical-align": KEYWORD,
        width: SIZE,
      },
    },
    transformTags: {
      a: (tagName, attribs) => ({
        tagName,
        attribs:
          attribs.target === "_blank"
            ? { ...attribs, rel: "noopener noreferrer nofollow" }
            : attribs,
      }),
    },
  });
}
