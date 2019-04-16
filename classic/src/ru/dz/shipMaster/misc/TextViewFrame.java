package ru.dz.shipMaster.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * Used to present texts to user.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class TextViewFrame extends JFrame {
    JEditorPane textPane = new JEditorPane("text/html","");
	private String text = "";
	
	public TextViewFrame(String htmlText) {
		super();
		this.text = htmlText;
		initialize();
		textPane.setText(htmlText);
	}

	
	private JPanel getButtons()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton copyButton = new JButton(); 
		panel.add(copyButton);
		
		copyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				copyAction();				
			}
		});
		copyButton.setText("Copy to clipboard");
		
		return panel;
	}

	
	protected void copyAction() {
        //StringSelection ss = new StringSelection(text);
		HtmlSelection ss = new HtmlSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}


	private void initialize() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(400, 300));
        
        this.setTitle("Gardemarine");
        this.setIconImage(VisualHelpers.getApplicationIconImage());
        this.setLocationByPlatform(true);

        this.setResizable(false);
        
        JPanel contentPane = new JPanel(new BorderLayout());

        textPane.setEditable(false);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(textPane);

        
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(getButtons(), BorderLayout.SOUTH);
        
        
        //this.setContentPane(scrollPane);
        this.add(contentPane);

        
        //JLabel pic = new JLabel(new ImageIcon(iPictogram));
		//contentPane.add(pic);
		
        //JTextArea text = new JTextArea("<b>bold</b> <i>italic</i>");

		
		this.pack();
		this.setVisible(true);
	}
	

	
    public static void main(String[] args) {
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
			} 
		catch (Exception e) {/* ignore inability to set l&f */}
	
		TextViewFrame frame = new TextViewFrame(
				"<html><body>\n"+
				"<table><th>No.</th><th>Dir</th><th>Name</th><th>Parameter</th><th>Legend</th><tr><td>0</td><td>Input</td><td>An. I 0</td><td>power.220.voltage</td><td>Напряжение (220)</td></tr><tr><td>1</td><td>Input</td><td>An. I 1</td><td>power.220.current</td><td>Ток (220)</td></tr><tr><td>2</td><td>Input</td><td>An. I 2</td><td>power.220.power</td><td>Мощность (220)</td></tr><tr><td>3</td><td>Input</td><td>An. I 3</td><td>power.220.freq</td><td>Частота (220)</td></tr><tr><td>4</td><td>Input</td><td>An. I 4</td></tr><tr><td>5</td><td>Input</td><td>An. I 5</td></tr><tr><td>6</td><td>Input</td><td>An. I 6</td></tr><tr><td>7</td><td>Input</td><td>An. I 7</td></tr><tr><td>8</td><td>Input</td><td>An. I 8</td></tr><tr><td>9</td><td>Input</td><td>An. I 9</td></tr><tr><td>10</td><td>Input</td><td>An. I 10</td></tr><tr><td>11</td><td>Input</td><td>An. I 11</td></tr><tr><td>12</td><td>Input</td><td>An. I 12</td></tr><tr><td>13</td><td>Input</td><td>An. I 13</td></tr><tr><td>14</td><td>Input</td><td>An. I 14</td></tr><tr><td>15</td><td>Input</td><td>An. I 15</td></tr><tr><td>16</td><td>Input</td><td>An. I 16</td></tr><tr><td>17</td><td>Input</td><td>An. I 17</td></tr><tr><td>18</td><td>Input</td><td>An. I 18</td></tr><tr><td>19</td><td>Input</td><td>An. I 19</td></tr><tr><td>20</td><td>Input</td><td>An. I 20</td></tr><tr><td>21</td><td>Input</td><td>An. I 21</td></tr><tr><td>22</td><td>Input</td><td>An. I 22</td></tr><tr><td>23</td><td>Input</td><td>An. I 23</td></tr><tr><td>24</td><td>Input</td><td>An. I 24</td></tr><tr><td>25</td><td>Input</td><td>An. I 25</td></tr><tr><td>26</td><td>Input</td><td>An. I 26</td></tr><tr><td>27</td><td>Input</td><td>An. I 27</td></tr><tr><td>28</td><td>Input</td><td>An. I 28</td></tr><tr><td>29</td><td>Input</td><td>An. I 29</td></tr><tr><td>30</td><td>Input</td><td>An. I 30</td></tr><tr><td>31</td><td>Input</td><td>An. I 31</td></tr><tr><td>32</td><td>Input</td><td>An. I 32</td></tr><tr><td>33</td><td>Input</td><td>An. I 33</td></tr><tr><td>34</td><td>Input</td><td>An. I 34</td></tr><tr><td>35</td><td>Input</td><td>An. I 35</td></tr><tr><td>36</td><td>Input</td><td>An. I 36</td></tr><tr><td>37</td><td>Input</td><td>An. I 37</td></tr><tr><td>38</td><td>Input</td><td>An. I 38</td></tr><tr><td>39</td><td>Input</td><td>An. I 39</td></tr><tr><td>40</td><td>Input</td><td>An. I 40</td></tr><tr><td>41</td><td>Input</td><td>An. I 41</td></tr><tr><td>42</td><td>Input</td><td>An. I 42</td></tr><tr><td>43</td><td>Input</td><td>An. I 43</td></tr><tr><td>44</td><td>Input</td><td>An. I 44</td></tr><tr><td>45</td><td>Input</td><td>An. I 45</td></tr><tr><td>46</td><td>Input</td><td>An. I 46</td></tr><tr><td>47</td><td>Input</td><td>An. I 47</td></tr><tr><td>48</td><td>Input</td><td>An. I 48</td></tr><tr><td>49</td><td>Input</td><td>An. I 49</td></tr><tr><td>50</td><td>Input</td><td>An. I 50</td></tr><tr><td>51</td><td>Input</td><td>An. I 51</td></tr><tr><td>52</td><td>Input</td><td>An. I 52</td></tr><tr><td>53</td><td>Input</td><td>An. I 53</td></tr><tr><td>54</td><td>Input</td><td>An. I 54</td></tr><tr><td>55</td><td>Input</td><td>An. I 55</td></tr><tr><td>56</td><td>Input</td><td>An. I 56</td></tr><tr><td>57</td><td>Input</td><td>An. I 57</td></tr><tr><td>58</td><td>Input</td><td>An. I 58</td></tr><tr><td>59</td><td>Input</td><td>An. I 59</td></tr><tr><td>60</td><td>Input</td><td>An. I 60</td></tr><tr><td>61</td><td>Input</td><td>An. I 61</td></tr><tr><td>62</td><td>Input</td><td>An. I 62</td></tr><tr><td>63</td><td>Input</td><td>An. I 63</td></tr><tr><td>64</td><td>Output</td><td>An. O 0</td></tr><tr><td>65</td><td>Output</td><td>An. O 1</td></tr><tr><td>66</td><td>Output</td><td>An. O 2</td></tr><tr><td>67</td><td>Output</td><td>An. O 3</td></tr><tr><td>68</td><td>Output</td><td>An. O 4</td></tr><tr><td>69</td><td>Output</td><td>An. O 5</td></tr><tr><td>70</td><td>Output</td><td>An. O 6</td></tr><tr><td>71</td><td>Output</td><td>An. O 7</td></tr><tr><td>72</td><td>Output</td><td>An. O 8</td></tr><tr><td>73</td><td>Output</td><td>An. O 9</td></tr><tr><td>74</td><td>Output</td><td>An. O 10</td></tr><tr><td>75</td><td>Output</td><td>An. O 11</td></tr><tr><td>76</td><td>Output</td><td>An. O 12</td></tr><tr><td>77</td><td>Output</td><td>An. O 13</td></tr><tr><td>78</td><td>Output</td><td>An. O 14</td></tr><tr><td>79</td><td>Output</td><td>An. O 15</td></tr><tr><td>80</td><td>Output</td><td>An. O 16</td></tr><tr><td>81</td><td>Output</td><td>An. O 17</td></tr><tr><td>82</td><td>Output</td><td>An. O 18</td></tr><tr><td>83</td><td>Output</td><td>An. O 19</td></tr><tr><td>84</td><td>Output</td><td>An. O 20</td></tr><tr><td>85</td><td>Output</td><td>An. O 21</td></tr><tr><td>86</td><td>Output</td><td>An. O 22</td></tr><tr><td>87</td><td>Output</td><td>An. O 23</td></tr><tr><td>88</td><td>Output</td><td>An. O 24</td></tr><tr><td>89</td><td>Output</td><td>An. O 25</td></tr><tr><td>90</td><td>Output</td><td>An. O 26</td></tr><tr><td>91</td><td>Output</td><td>An. O 27</td></tr><tr><td>92</td><td>Output</td><td>An. O 28</td></tr><tr><td>93</td><td>Output</td><td>An. O 29</td></tr><tr><td>94</td><td>Output</td><td>An. O 30</td></tr><tr><td>95</td><td>Output</td><td>An. O 31</td></tr><tr><td>96</td><td>Output</td><td>An. O 32</td></tr><tr><td>97</td><td>Output</td><td>An. O 33</td></tr><tr><td>98</td><td>Output</td><td>An. O 34</td></tr><tr><td>99</td><td>Output</td><td>An. O 35</td></tr><tr><td>100</td><td>Output</td><td>An. O 36</td></tr><tr><td>101</td><td>Output</td><td>An. O 37</td></tr><tr><td>102</td><td>Output</td><td>An. O 38</td></tr><tr><td>103</td><td>Output</td><td>An. O 39</td></tr><tr><td>104</td><td>Output</td><td>An. O 40</td></tr><tr><td>105</td><td>Output</td><td>An. O 41</td></tr><tr><td>106</td><td>Output</td><td>An. O 42</td></tr><tr><td>107</td><td>Output</td><td>An. O 43</td></tr><tr><td>108</td><td>Output</td><td>An. O 44</td></tr><tr><td>109</td><td>Output</td><td>An. O 45</td></tr><tr><td>110</td><td>Output</td><td>An. O 46</td></tr><tr><td>111</td><td>Output</td><td>An. O 47</td></tr><tr><td>112</td><td>Output</td><td>An. O 48</td></tr><tr><td>113</td><td>Output</td><td>An. O 49</td></tr><tr><td>114</td><td>Output</td><td>An. O 50</td></tr><tr><td>115</td><td>Output</td><td>An. O 51</td></tr><tr><td>116</td><td>Output</td><td>An. O 52</td></tr><tr><td>117</td><td>Output</td><td>An. O 53</td></tr><tr><td>118</td><td>Output</td><td>An. O 54</td></tr><tr><td>119</td><td>Output</td><td>An. O 55</td></tr><tr><td>120</td><td>Output</td><td>An. O 56</td></tr><tr><td>121</td><td>Output</td><td>An. O 57</td></tr><tr><td>122</td><td>Output</td><td>An. O 58</td></tr><tr><td>123</td><td>Output</td><td>An. O 59</td></tr><tr><td>124</td><td>Output</td><td>An. O 60</td></tr><tr><td>125</td><td>Output</td><td>An. O 61</td></tr><tr><td>126</td><td>Output</td><td>An. O 62</td></tr><tr><td>127</td><td>Output</td><td>An. O 63</td></tr><tr><td>128</td><td>Input</td><td>Dig. I 0</td><td>info.power.shore</td><td>Питание от берега</td></tr><tr><td>129</td><td>Input</td><td>Dig. I 1</td><td>info.power.generator</td><td>Питание от ДГ</td></tr><tr><td>130</td><td>Input</td><td>Dig. I 2</td><td>info.power.24.main</td><td>Основное питание 24В</td></tr><tr><td>131</td><td>Input</td><td>Dig. I 3</td><td>info.power.24.reserve</td><td>Резервное питание 24В</td></tr><tr><td>132</td><td>Input</td><td>Dig. I 4</td><td>info.power.24.insulation.fault</td><td>Сопротивление изоляции 24В ниже нормы</td></tr><tr><td>133</td><td>Input</td><td>Dig. I 5</td><td>info.power.220.insulation.fault</td><td>info.power.220.insulation.fault</td></tr><tr><td>134</td><td>Input</td><td>Dig. I 6</td><td>alarm.water.forepeak</td><td>alarm.water.forepeak</td></tr><tr><td>135</td><td>Input</td><td>Dig. I 7</td><td>alarm.water.salon</td><td>alarm.water.salon</td></tr><tr><td>136</td><td>Input</td><td>Dig. I 8</td><td>alarm.water.tanks</td><td>alarm.water.tanks</td></tr><tr><td>137</td><td>Input</td><td>Dig. I 9</td><td>alarm.water.machine</td><td>alarm.water.machine</td></tr><tr><td>138</td><td>Input</td><td>Dig. I 10</td><td>info.pump.on</td><td>info.pump.on</td></tr><tr><td>139</td><td>Input</td><td>Dig. I 11</td><td>info.pump.fault</td><td>info.pump.fault</td></tr><tr><td>140</td><td>Input</td><td>Dig. I 12</td><td>info.pump.level</td><td>Уровень не снижается</td></tr><tr><td>141</td><td>Input</td><td>Dig. I 13</td><td>tanks.fuel.lo</td><td>< 20%</td></tr><tr><td>142</td><td>Input</td><td>Dig. I 14</td><td>tanks.fuel.hi</td><td>> 80%</td></tr><tr><td>143</td><td>Input</td><td>Dig. I 15</td><td>tanks.water.lo</td><td>вода < 20%</td></tr><tr><td>144</td><td>Input</td><td>Dig. I 16</td><td>tanks.water.hi</td><td>вода > 80%</td></tr><tr><td>145</td><td>Input</td><td>Dig. I 17</td><td>tanks.waste.lo</td><td>сточн < 20%</td></tr><tr><td>146</td><td>Input</td><td>Dig. I 18</td><td>tanks.waste.hi</td><td>сточн > 80%</td></tr><tr><td>147</td><td>Input</td><td>Dig. I 19</td></tr><tr><td>148</td><td>Input</td><td>Dig. I 20</td></tr><tr><td>149</td><td>Input</td><td>Dig. I 21</td></tr><tr><td>150</td><td>Input</td><td>Dig. I 22</td></tr><tr><td>151</td><td>Input</td><td>Dig. I 23</td></tr><tr><td>152</td><td>Input</td><td>Dig. I 24</td></tr><tr><td>153</td><td>Input</td><td>Dig. I 25</td></tr><tr><td>154</td><td>Input</td><td>Dig. I 26</td></tr><tr><td>155</td><td>Input</td><td>Dig. I 27</td></tr><tr><td>156</td><td>Input</td><td>Dig. I 28</td></tr><tr><td>157</td><td>Input</td><td>Dig. I 29</td></tr><tr><td>158</td><td>Input</td><td>Dig. I 30</td></tr><tr><td>159</td><td>Input</td><td>Dig. I 31</td></tr><tr><td>160</td><td>Input</td><td>Dig. I 32</td></tr><tr><td>161</td><td>Input</td><td>Dig. I 33</td></tr><tr><td>162</td><td>Input</td><td>Dig. I 34</td></tr><tr><td>163</td><td>Input</td><td>Dig. I 35</td></tr><tr><td>164</td><td>Input</td><td>Dig. I 36</td></tr><tr><td>165</td><td>Input</td><td>Dig. I 37</td></tr><tr><td>166</td><td>Input</td><td>Dig. I 38</td></tr><tr><td>167</td><td>Input</td><td>Dig. I 39</td></tr><tr><td>168</td><td>Input</td><td>Dig. I 40</td></tr><tr><td>169</td><td>Input</td><td>Dig. I 41</td></tr><tr><td>170</td><td>Input</td><td>Dig. I 42</td></tr><tr><td>171</td><td>Input</td><td>Dig. I 43</td></tr><tr><td>172</td><td>Input</td><td>Dig. I 44</td></tr><tr><td>173</td><td>Input</td><td>Dig. I 45</td></tr><tr><td>174</td><td>Input</td><td>Dig. I 46</td></tr><tr><td>175</td><td>Input</td><td>Dig. I 47</td></tr><tr><td>176</td><td>Input</td><td>Dig. I 48</td></tr><tr><td>177</td><td>Input</td><td>Dig. I 49</td></tr><tr><td>178</td><td>Input</td><td>Dig. I 50</td></tr><tr><td>179</td><td>Input</td><td>Dig. I 51</td></tr><tr><td>180</td><td>Input</td><td>Dig. I 52</td></tr><tr><td>181</td><td>Input</td><td>Dig. I 53</td></tr><tr><td>182</td><td>Input</td><td>Dig. I 54</td></tr><tr><td>183</td><td>Input</td><td>Dig. I 55</td></tr><tr><td>184</td><td>Input</td><td>Dig. I 56</td></tr><tr><td>185</td><td>Input</td><td>Dig. I 57</td></tr><tr><td>186</td><td>Input</td><td>Dig. I 58</td></tr><tr><td>187</td><td>Input</td><td>Dig. I 59</td></tr><tr><td>188</td><td>Input</td><td>Dig. I 60</td></tr><tr><td>189</td><td>Input</td><td>Dig. I 61</td></tr><tr><td>190</td><td>Input</td><td>Dig. I 62</td></tr><tr><td>191</td><td>Input</td><td>Dig. I 63</td></tr><tr><td>192</td><td>Input</td><td>Dig. I 64</td></tr><tr><td>193</td><td>Input</td><td>Dig. I 65</td></tr><tr><td>194</td><td>Input</td><td>Dig. I 66</td></tr><tr><td>195</td><td>Input</td><td>Dig. I 67</td></tr><tr><td>196</td><td>Input</td><td>Dig. I 68</td></tr><tr><td>197</td><td>Input</td><td>Dig. I 69</td></tr><tr><td>198</td><td>Input</td><td>Dig. I 70</td></tr><tr><td>199</td><td>Input</td><td>Dig. I 71</td></tr><tr><td>200</td><td>Input</td><td>Dig. I 72</td></tr><tr><td>201</td><td>Input</td><td>Dig. I 73</td></tr><tr><td>202</td><td>Input</td><td>Dig. I 74</td></tr><tr><td>203</td><td>Input</td><td>Dig. I 75</td></tr><tr><td>204</td><td>Input</td><td>Dig. I 76</td></tr><tr><td>205</td><td>Input</td><td>Dig. I 77</td></tr><tr><td>206</td><td>Input</td><td>Dig. I 78</td></tr><tr><td>207</td><td>Input</td><td>Dig. I 79</td></tr><tr><td>208</td><td>Input</td><td>Dig. I 80</td></tr><tr><td>209</td><td>Input</td><td>Dig. I 81</td></tr><tr><td>210</td><td>Input</td><td>Dig. I 82</td></tr><tr><td>211</td><td>Input</td><td>Dig. I 83</td></tr><tr><td>212</td><td>Input</td><td>Dig. I 84</td></tr><tr><td>213</td><td>Input</td><td>Dig. I 85</td></tr><tr><td>214</td><td>Input</td><td>Dig. I 86</td></tr><tr><td>215</td><td>Input</td><td>Dig. I 87</td></tr><tr><td>216</td><td>Input</td><td>Dig. I 88</td></tr><tr><td>217</td><td>Input</td><td>Dig. I 89</td></tr><tr><td>218</td><td>Input</td><td>Dig. I 90</td></tr><tr><td>219</td><td>Input</td><td>Dig. I 91</td></tr><tr><td>220</td><td>Input</td><td>Dig. I 92</td></tr><tr><td>221</td><td>Input</td><td>Dig. I 93</td></tr><tr><td>222</td><td>Input</td><td>Dig. I 94</td></tr><tr><td>223</td><td>Input</td><td>Dig. I 95</td></tr><tr><td>224</td><td>Input</td><td>Dig. I 96</td></tr><tr><td>225</td><td>Input</td><td>Dig. I 97</td></tr><tr><td>226</td><td>Input</td><td>Dig. I 98</td></tr><tr><td>227</td><td>Input</td><td>Dig. I 99</td></tr><tr><td>228</td><td>Input</td><td>Dig. I 100</td></tr><tr><td>229</td><td>Input</td><td>Dig. I 101</td></tr><tr><td>230</td><td>Input</td><td>Dig. I 102</td></tr><tr><td>231</td><td>Input</td><td>Dig. I 103</td></tr><tr><td>232</td><td>Input</td><td>Dig. I 104</td></tr><tr><td>233</td><td>Input</td><td>Dig. I 105</td></tr><tr><td>234</td><td>Input</td><td>Dig. I 106</td></tr><tr><td>235</td><td>Input</td><td>Dig. I 107</td></tr><tr><td>236</td><td>Input</td><td>Dig. I 108</td></tr><tr><td>237</td><td>Input</td><td>Dig. I 109</td></tr><tr><td>238</td><td>Input</td><td>Dig. I 110</td></tr><tr><td>239</td><td>Input</td><td>Dig. I 111</td></tr><tr><td>240</td><td>Input</td><td>Dig. I 112</td></tr><tr><td>241</td><td>Input</td><td>Dig. I 113</td></tr><tr><td>242</td><td>Input</td><td>Dig. I 114</td></tr><tr><td>243</td><td>Input</td><td>Dig. I 115</td></tr><tr><td>244</td><td>Input</td><td>Dig. I 116</td></tr><tr><td>245</td><td>Input</td><td>Dig. I 117</td></tr><tr><td>246</td><td>Input</td><td>Dig. I 118</td></tr><tr><td>247</td><td>Input</td><td>Dig. I 119</td></tr><tr><td>248</td><td>Input</td><td>Dig. I 120</td></tr><tr><td>249</td><td>Input</td><td>Dig. I 121</td></tr><tr><td>250</td><td>Input</td><td>Dig. I 122</td></tr><tr><td>251</td><td>Input</td><td>Dig. I 123</td></tr><tr><td>252</td><td>Input</td><td>Dig. I 124</td></tr><tr><td>253</td><td>Input</td><td>Dig. I 125</td></tr><tr><td>254</td><td>Input</td><td>Dig. I 126</td></tr><tr><td>255</td><td>Input</td><td>Dig. I 127</td></tr><tr><td>256</td><td>Output</td><td>Dig. O 0</td><td>info.light.main.boards</td><td>БОРТ</td></tr><tr><td>257</td><td>Output</td><td>Dig. O 1</td><td>info.light.main.top_rear</td><td>ТОП+КОРМА</td></tr><tr><td>258</td><td>Output</td><td>Dig. O 2</td><td>info.light.main.white</td><td>БЕЛЫЙ</td></tr><tr><td>259</td><td>Output</td><td>Dig. O 3</td><td>info.light.reserve.boards</td><td>БОРТ</td></tr><tr><td>260</td><td>Output</td><td>Dig. O 4</td><td>info.light.reserve.top_rear</td><td>ТОП+КОРМА</td></tr><tr><td>261</td><td>Output</td><td>Dig. O 5</td><td>info.light.reserve.white</td><td>БЕЛЫЙ</td></tr><tr><td>262</td><td>Output</td><td>Dig. O 6</td><td>command.heater.front</td><td>Обогрев нос</td></tr><tr><td>263</td><td>Output</td><td>Dig. O 7</td><td>command.heater.rear</td><td>Обогрев корма</td></tr><tr><td>264</td><td>Output</td><td>Dig. O 8</td><td>command.heater.engine</td><td>Прогрев двигателя</td></tr><tr><td>265</td><td>Output</td><td>Dig. O 9</td><td>command.horn.move</td><td>ХОД</td></tr><tr><td>266</td><td>Output</td><td>Dig. O 10</td><td>command.horn.stop</td><td>ОСТАНОВКА</td></tr><tr><td>267</td><td>Output</td><td>Dig. O 11</td><td>command.horn.trail</td><td>БУКСИРОВКА</td></tr><tr><td>268</td><td>Output</td><td>Dig. O 12</td><td>command.horn.anchor</td><td>НА ЯКОРЕ</td></tr><tr><td>269</td><td>Output</td><td>Dig. O 13</td><td>command.horn.failed</td><td>ОТКАЗ</td></tr><tr><td>270</td><td>Output</td><td>Dig. O 14</td><td>command.misc.QF1</td><td>QF1</td></tr><tr><td>271</td><td>Output</td><td>Dig. O 15</td><td>command.misc.QF2</td><td>QF2</td></tr><tr><td>272</td><td>Output</td><td>Dig. O 16</td><td>command.misc.QF3</td><td>QF3</td></tr><tr><td>273</td><td>Output</td><td>Dig. O 17</td><td>command.misc.QF4</td><td>QF4</td></tr><tr><td>274</td><td>Output</td><td>Dig. O 18</td><td>command.misc.QF5</td><td>QF5</td></tr><tr><td>275</td><td>Output</td><td>Dig. O 19</td><td>command.misc.QF39</td><td>QF39</td></tr><tr><td>276</td><td>Output</td><td>Dig. O 20</td><td>command.power.24.insulation.reset</td><td>Сброс </td></tr><tr><td>277</td><td>Output</td><td>Dig. O 21</td><td>command.power.220.insulation.reset</td><td>Сброс</td></tr><tr><td>278</td><td>Output</td><td>Dig. O 22</td></tr><tr><td>279</td><td>Output</td><td>Dig. O 23</td></tr><tr><td>280</td><td>Output</td><td>Dig. O 24</td></tr><tr><td>281</td><td>Output</td><td>Dig. O 25</td></tr><tr><td>282</td><td>Output</td><td>Dig. O 26</td></tr><tr><td>283</td><td>Output</td><td>Dig. O 27</td></tr><tr><td>284</td><td>Output</td><td>Dig. O 28</td></tr><tr><td>285</td><td>Output</td><td>Dig. O 29</td></tr><tr><td>286</td><td>Output</td><td>Dig. O 30</td></tr><tr><td>287</td><td>Output</td><td>Dig. O 31</td></tr><tr><td>288</td><td>Output</td><td>Dig. O 32</td></tr><tr><td>289</td><td>Output</td><td>Dig. O 33</td></tr><tr><td>290</td><td>Output</td><td>Dig. O 34</td></tr><tr><td>291</td><td>Output</td><td>Dig. O 35</td></tr><tr><td>292</td><td>Output</td><td>Dig. O 36</td></tr><tr><td>293</td><td>Output</td><td>Dig. O 37</td></tr><tr><td>294</td><td>Output</td><td>Dig. O 38</td></tr><tr><td>295</td><td>Output</td><td>Dig. O 39</td></tr><tr><td>296</td><td>Output</td><td>Dig. O 40</td></tr><tr><td>297</td><td>Output</td><td>Dig. O 41</td></tr><tr><td>298</td><td>Output</td><td>Dig. O 42</td></tr><tr><td>299</td><td>Output</td><td>Dig. O 43</td></tr><tr><td>300</td><td>Output</td><td>Dig. O 44</td></tr><tr><td>301</td><td>Output</td><td>Dig. O 45</td></tr><tr><td>302</td><td>Output</td><td>Dig. O 46</td></tr><tr><td>303</td><td>Output</td><td>Dig. O 47</td></tr><tr><td>304</td><td>Output</td><td>Dig. O 48</td></tr><tr><td>305</td><td>Output</td><td>Dig. O 49</td></tr><tr><td>306</td><td>Output</td><td>Dig. O 50</td></tr><tr><td>307</td><td>Output</td><td>Dig. O 51</td></tr><tr><td>308</td><td>Output</td><td>Dig. O 52</td></tr><tr><td>309</td><td>Output</td><td>Dig. O 53</td></tr><tr><td>310</td><td>Output</td><td>Dig. O 54</td></tr><tr><td>311</td><td>Output</td><td>Dig. O 55</td></tr><tr><td>312</td><td>Output</td><td>Dig. O 56</td></tr><tr><td>313</td><td>Output</td><td>Dig. O 57</td></tr><tr><td>314</td><td>Output</td><td>Dig. O 58</td></tr><tr><td>315</td><td>Output</td><td>Dig. O 59</td></tr><tr><td>316</td><td>Output</td><td>Dig. O 60</td></tr><tr><td>317</td><td>Output</td><td>Dig. O 61</td></tr><tr><td>318</td><td>Output</td><td>Dig. O 62</td></tr><tr><td>319</td><td>Output</td><td>Dig. O 63</td></tr><tr><td>320</td><td>Output</td><td>Dig. O 64</td></tr><tr><td>321</td><td>Output</td><td>Dig. O 65</td></tr><tr><td>322</td><td>Output</td><td>Dig. O 66</td></tr><tr><td>323</td><td>Output</td><td>Dig. O 67</td></tr><tr><td>324</td><td>Output</td><td>Dig. O 68</td></tr><tr><td>325</td><td>Output</td><td>Dig. O 69</td></tr><tr><td>326</td><td>Output</td><td>Dig. O 70</td></tr><tr><td>327</td><td>Output</td><td>Dig. O 71</td></tr><tr><td>328</td><td>Output</td><td>Dig. O 72</td></tr><tr><td>329</td><td>Output</td><td>Dig. O 73</td></tr><tr><td>330</td><td>Output</td><td>Dig. O 74</td></tr><tr><td>331</td><td>Output</td><td>Dig. O 75</td></tr><tr><td>332</td><td>Output</td><td>Dig. O 76</td></tr><tr><td>333</td><td>Output</td><td>Dig. O 77</td></tr><tr><td>334</td><td>Output</td><td>Dig. O 78</td></tr><tr><td>335</td><td>Output</td><td>Dig. O 79</td></tr><tr><td>336</td><td>Output</td><td>Dig. O 80</td></tr><tr><td>337</td><td>Output</td><td>Dig. O 81</td></tr><tr><td>338</td><td>Output</td><td>Dig. O 82</td></tr><tr><td>339</td><td>Output</td><td>Dig. O 83</td></tr><tr><td>340</td><td>Output</td><td>Dig. O 84</td></tr><tr><td>341</td><td>Output</td><td>Dig. O 85</td></tr><tr><td>342</td><td>Output</td><td>Dig. O 86</td></tr><tr><td>343</td><td>Output</td><td>Dig. O 87</td></tr><tr><td>344</td><td>Output</td><td>Dig. O 88</td></tr><tr><td>345</td><td>Output</td><td>Dig. O 89</td></tr><tr><td>346</td><td>Output</td><td>Dig. O 90</td></tr><tr><td>347</td><td>Output</td><td>Dig. O 91</td></tr><tr><td>348</td><td>Output</td><td>Dig. O 92</td></tr><tr><td>349</td><td>Output</td><td>Dig. O 93</td></tr><tr><td>350</td><td>Output</td><td>Dig. O 94</td></tr><tr><td>351</td><td>Output</td><td>Dig. O 95</td></tr><tr><td>352</td><td>Output</td><td>Dig. O 96</td></tr><tr><td>353</td><td>Output</td><td>Dig. O 97</td></tr><tr><td>354</td><td>Output</td><td>Dig. O 98</td></tr><tr><td>355</td><td>Output</td><td>Dig. O 99</td></tr><tr><td>356</td><td>Output</td><td>Dig. O 100</td></tr><tr><td>357</td><td>Output</td><td>Dig. O 101</td></tr><tr><td>358</td><td>Output</td><td>Dig. O 102</td></tr><tr><td>359</td><td>Output</td><td>Dig. O 103</td></tr><tr><td>360</td><td>Output</td><td>Dig. O 104</td></tr><tr><td>361</td><td>Output</td><td>Dig. O 105</td></tr><tr><td>362</td><td>Output</td><td>Dig. O 106</td></tr><tr><td>363</td><td>Output</td><td>Dig. O 107</td></tr><tr><td>364</td><td>Output</td><td>Dig. O 108</td></tr><tr><td>365</td><td>Output</td><td>Dig. O 109</td></tr><tr><td>366</td><td>Output</td><td>Dig. O 110</td></tr><tr><td>367</td><td>Output</td><td>Dig. O 111</td></tr><tr><td>368</td><td>Output</td><td>Dig. O 112</td></tr><tr><td>369</td><td>Output</td><td>Dig. O 113</td></tr><tr><td>370</td><td>Output</td><td>Dig. O 114</td></tr><tr><td>371</td><td>Output</td><td>Dig. O 115</td></tr><tr><td>372</td><td>Output</td><td>Dig. O 116</td></tr><tr><td>373</td><td>Output</td><td>Dig. O 117</td></tr><tr><td>374</td><td>Output</td><td>Dig. O 118</td></tr><tr><td>375</td><td>Output</td><td>Dig. O 119</td></tr><tr><td>376</td><td>Output</td><td>Dig. O 120</td></tr><tr><td>377</td><td>Output</td><td>Dig. O 121</td></tr><tr><td>378</td><td>Output</td><td>Dig. O 122</td></tr><tr><td>379</td><td>Output</td><td>Dig. O 123</td></tr><tr><td>380</td><td>Output</td><td>Dig. O 124</td></tr><tr><td>381</td><td>Output</td><td>Dig. O 125</td></tr><tr><td>382</td><td>Output</td><td>Dig. O 126</td></tr><tr><td>383</td><td>Output</td><td>Dig. O 127</td></tr><tr><td>384</td><td>Input</td><td>PFC I 0</td></tr><tr><td>385</td><td>Input</td><td>PFC I 1</td></tr><tr><td>386</td><td>Input</td><td>PFC I 2</td></tr><tr><td>387</td><td>Input</td><td>PFC I 3</td></tr><tr><td>388</td><td>Input</td><td>PFC I 4</td></tr><tr><td>389</td><td>Input</td><td>PFC I 5</td></tr><tr><td>390</td><td>Input</td><td>PFC I 6</td></tr><tr><td>391</td><td>Input</td><td>PFC I 7</td></tr><tr><td>392</td><td>Input</td><td>PFC I 8</td></tr><tr><td>393</td><td>Input</td><td>PFC I 9</td></tr><tr><td>394</td><td>Input</td><td>PFC I 10</td></tr><tr><td>395</td><td>Input</td><td>PFC I 11</td></tr><tr><td>396</td><td>Input</td><td>PFC I 12</td></tr><tr><td>397</td><td>Input</td><td>PFC I 13</td></tr><tr><td>398</td><td>Input</td><td>PFC I 14</td></tr><tr><td>399</td><td>Input</td><td>PFC I 15</td></tr><tr><td>400</td><td>Input</td><td>PFC I 16</td></tr><tr><td>401</td><td>Input</td><td>PFC I 17</td></tr><tr><td>402</td><td>Input</td><td>PFC I 18</td></tr><tr><td>403</td><td>Input</td><td>PFC I 19</td></tr><tr><td>404</td><td>Input</td><td>PFC I 20</td></tr><tr><td>405</td><td>Input</td><td>PFC I 21</td></tr><tr><td>406</td><td>Input</td><td>PFC I 22</td></tr><tr><td>407</td><td>Input</td><td>PFC I 23</td></tr><tr><td>408</td><td>Input</td><td>PFC I 24</td></tr><tr><td>409</td><td>Input</td><td>PFC I 25</td></tr><tr><td>410</td><td>Input</td><td>PFC I 26</td></tr><tr><td>411</td><td>Input</td><td>PFC I 27</td></tr><tr><td>412</td><td>Input</td><td>PFC I 28</td></tr><tr><td>413</td><td>Input</td><td>PFC I 29</td></tr><tr><td>414</td><td>Input</td><td>PFC I 30</td></tr><tr><td>415</td><td>Input</td><td>PFC I 31</td></tr></table>\n"+
				"</body></html>\n"

				/*
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n" +
				"Some <b>bold</b> text. <br>\n"
				/* */ 
				);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    }	
}
