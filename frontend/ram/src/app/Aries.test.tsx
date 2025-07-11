import "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import Aries from "./page";

jest.mock("@mantine/core", () => ({
  Stack: ({ children }: any) => <div data-testid="stack">{children}</div>,
  Center: ({ children }: any) => <div data-testid="center">{children}</div>,
  Box: ({ children, ...props }: any) => (
    <div data-testid="box" {...props}>
      {children}
    </div>
  ),
  Button: ({ children, ...props }: any) => (
    <button {...props}>{children}</button>
  ),
  Image: ({ src, ...props }: any) => <img src={src} alt="mock" {...props} />,
  Group: ({ children }: any) => <div data-testid="group">{children}</div>,
  useMantineColorScheme: () => ({
    colorScheme: "light",
    setColorScheme: jest.fn(),
  }),
}));

jest.mock("@mantine/hooks", () => ({
  useToggle: (values: string[]) => [values[0], jest.fn()],
}));

jest.mock("./theme", () => ({
  AriesThemeSet: { colors: ["light", "dark"] },
}));

describe("Aries component", () => {
  it("renders the image with correct src", () => {
    render(<Aries />);
    const image = screen.getByRole("img");
    expect(image).toBeInTheDocument();
    expect(image).toHaveAttribute(
      "src",
      "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-7.png"
    );
  });

  it("renders Stack and Box components", () => {
    render(<Aries />);
    const stack = screen.getByTestId("stack");
    const boxes = screen.getAllByTestId("box");
    expect(stack).toBeInTheDocument();
    expect(boxes).toHaveLength(2);
  });
});
