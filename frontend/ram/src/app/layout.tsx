import "@mantine/core/styles.css";
import "./globals.css";

import React from "react";
import {
  ColorSchemeScript,
  mantineHtmlProps,
  MantineProvider,
} from "@mantine/core";

import AriesLayout from "@/app/components/AriesLayout";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { theme } from "./theme";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const layoutProps: AriesLayoutProps = {
    navbarItems: [
      { label: "Home", href: "/" },
      { label: "Posts", href: "/posts" },
      { label: "About", href: "/about" },
    ],
  };

  return (
    <html lang="en" {...mantineHtmlProps}>
      <head>
        <ColorSchemeScript defaultColorScheme="auto" />
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
