import "@mantine/core/styles.css";
import "./globals.css";

import React from "react";
import {
  createTheme,
  ColorSchemeScript,
  MantineProvider,
  mantineHtmlProps,
} from "@mantine/core";

import AriesLayout from "@/app/components/AriesLayout";
import { LayoutProps } from "@/app/types/AriesLayoutProperties";

const theme = createTheme({
  /** Your theme override here */
  //fontFamily: "Open Sans, sans-serif",
  //  primaryColor: "cyan",
});

const layoutProps: LayoutProps = {
  navbarItems: [
    { label: "Home", href: "/" },
    { label: "Rambling shits", href: "/posts" },
    { label: "Contact", href: "/contact" },
  ],
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" {...mantineHtmlProps}>
      <head>
        <style>
          {`
            body {
              background-color: #e5e5f7;
              opacity: 0.8;
              background-size: 10px 10px;
              background-image: repeating-linear-gradient(45deg,hsl(237, 48.80%, 91.60%) 0,rgb(220, 221, 241) 1px, #e5e5f7 0, #e5e5f7 50%);
            }
          `}
        </style>
        <ColorSchemeScript />
      </head>
      <meta
        name="viewport"
        content="minimum-scale=1, initial-scale=1, width=device-width, user-scalable=no"
      />
      <body>
        <MantineProvider theme={theme}>
          <AriesLayout {...layoutProps}>{children}</AriesLayout>
        </MantineProvider>
      </body>
    </html>
  );
}
