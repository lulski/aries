import "@mantine/core/styles.css";
import "./globals.css";

import React from "react";
import {
  createTheme,
  ColorSchemeScript,
  MantineProvider,
  mantineHtmlProps,
  MantineColorsTuple,
} from "@mantine/core";

import AriesLayout from "@/app/components/AriesLayout";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";

const ariesColor: MantineColorsTuple = [
  "#ffeaf3",
  "#fcd4e1",
  "#f4a7bf",
  "#ec779c",
  "#e64f7e",
  "#e3366c",
  "#e22862",
  "#c91a52",
  "#b41148",
  "#9f003e",
];

const theme = createTheme({
  colors: { ariesColor },
  fontFamily: "Open Sans, sans-serif",
  primaryColor: "pink",
});

const layoutProps: AriesLayoutProps = {
  navbarItems: [
    { label: "Home", href: "/" },
    { label: "Posts", href: "/posts" },
    { label: "About", href: "/about" },
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
