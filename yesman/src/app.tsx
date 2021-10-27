import * as React from 'react'
import * as ReactDOM from 'react-dom'
import { createGlobalStyle } from 'styled-components'
import Index from './components/index'

const Global = createGlobalStyle`
  body{
    margin: 0;
    background-color: #272727;
  }
  *{
    box-sizing: border-box;
  }
`
const render = () => {
  ReactDOM.render(
    <React.Fragment>
      <Global />
      <Index />
    </React.Fragment>,
    document.body
  )
}
render()
