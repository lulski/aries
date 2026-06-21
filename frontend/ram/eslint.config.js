import nextPlugin from "@next/eslint-plugin-next";
import reactHooksPlugin from "eslint-plugin-react-hooks";

export default [
  {
    plugins: {
      "@next/next": nextPlugin,
      "react-hooks": reactHooksPlugin, // 👈 This registers the missing plugin
    },
    rules: {
      // ✅ This will now turn off the cascading render error successfully
      "react-hooks/set-state-in-effect": "off",
    },
  },
];
